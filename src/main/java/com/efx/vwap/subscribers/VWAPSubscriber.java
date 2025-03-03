package com.efx.vwap.subscribers;

import com.efx.vwap.calculators.Calculator;
import com.efx.vwap.calculators.VWAPCalculator;
import com.efx.vwap.log.LogConstants;
import com.efx.vwap.models.Instrument;
import com.efx.vwap.models.MarketDataEvent;
import com.efx.vwap.models.VWAPByInstrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * Subscriber to calculate VWAP of instruments (across venues)
 * This class is intentionally NOT thread safe hence multiple subscriber objects
 * need to be created for scaling if required.
 * This class does not produce any objects after initialization - and in production
 * all input/output to this class will be the same (currently loggers, publishers do produce objects)
 */
public class VWAPSubscriber implements Subscriber<MarketDataEvent> {

    //ideally this logger would be an async logger - again typically this would
    //write to a disruptor from where an appender would pick the message up.
    //wrapping the char arrays defined in @{LogConstants} as Strings just because
    //of the logger used. In production, the array would be copied to the array
    //on the disruptor
    private static final Logger logger = LoggerFactory.getLogger(VWAPSubscriber.class);

    private final EnumMap<Instrument, VWAPByInstrument> vwaps;

    private final Calculator<MarketDataEvent, VWAPByInstrument.VWAP,
            VWAPByInstrument.VWAP> vwapCalculator;

    private final VWAPByInstrument.VWAP resultHolder;

    private final Subscriber<VWAPByInstrument> vwapPublisher;

    public VWAPSubscriber() {
        this(new VWAPCalculator(), new VWAPPublisher());
    }

    public VWAPSubscriber(final Calculator<MarketDataEvent, VWAPByInstrument.VWAP, VWAPByInstrument.VWAP> vwapCalculator,
                          final Subscriber<VWAPByInstrument> vwapPublisher) {
        vwaps = new EnumMap<>(Instrument.class);
        this.vwapCalculator = vwapCalculator;
        resultHolder = new VWAPByInstrument.VWAP();
        this.vwapPublisher = vwapPublisher;

        Instrument[] configuredInstruments = getConfiguredInstruments();
        Arrays.stream(configuredInstruments).forEach(ins -> vwaps.put(ins, new VWAPByInstrument(ins)));
    }

    @Override
    public void onMessage(MarketDataEvent marketDataEvent) {

        if(marketDataEvent.getInstrument() == null) {
            logger.warn(String.valueOf(LogConstants.NULL_INSTRUMENT));
            return;
        }

        //we have a side, so should not get a negative quantity
        if (marketDataEvent.getQuantity() < 0) {
            logger.warn(String.valueOf(LogConstants.NEGATIVE_QUANTITY));
            return;
        }

        //we have a side, so should not get a negative price
        if (marketDataEvent.getPrice() <= 0) {
            logger.warn(String.valueOf(LogConstants.NEGATIVE_PRICE));
            return;
        }

        VWAPByInstrument fullVwap = vwaps.get(marketDataEvent.getInstrument());

        //instrument exists but we are not configured to process that instrument
        if(fullVwap == null) {

            // 7 - length of ccypair (Ex:- GBP/USD). can be replaced by length of char array
            System.arraycopy(LogConstants.INVALID_INSTRUMENT, 18,
                    Instrument.getInstrumentCharArray(marketDataEvent.getInstrument()), 0, 7);
            //not "resetting" the ccypair from array since we will overwerite it every time.
            logger.warn(String.valueOf(LogConstants.INVALID_INSTRUMENT));
            return;
        }

        VWAPByInstrument.VWAP vwap = null;

        switch (marketDataEvent.getSide()) {
            case SELL -> vwap = fullVwap.getSell();
            case BUY -> vwap = fullVwap.getBuy();
        }

        //an extra copy here - but prefer all mutations to the vwaps to be in one place
        //so passing the resultHolder to the calculator and then copying it back to
        //the correct vwap here.
        try {
            vwapCalculator.calculate(marketDataEvent, vwap, resultHolder);
            vwap.copy(resultHolder);
        } catch (Exception e) {
            logger.warn("Could not process tick");
            return;
        }

        try {
            vwapPublisher.onMessage(fullVwap);
        } catch (Exception e) {
            logger.warn("Could not publish processed tick");
        }

    }

    //Any filtering etc if required for instruments to be done here
    private Instrument[] getConfiguredInstruments() {
        return Instrument.values();
    }


}
