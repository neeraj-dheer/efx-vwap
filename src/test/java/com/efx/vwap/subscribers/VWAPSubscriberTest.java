package com.efx.vwap.subscribers;

import com.efx.vwap.calculators.VWAPCalculator;
import com.efx.vwap.models.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VWAPSubscriberTest {

    @Test
    public void testOneTick() {
        MarketDataEvent mde1 = new MarketDataEvent(10110000, 1_000_000, Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        VWAPByInstrument result = new VWAPByInstrument(Instrument.GBPUSD);
        result.getBuy().setCumulativePriceAndQty(10110000000000L);
        result.getBuy().setCumulativeQuantity(1_000_000);
        result.getBuy().setVwap(10110000);
        VWAPPublisherAsserter asserter = new VWAPPublisherAsserter();
        asserter.setVwapByInstrument(result);
        VWAPSubscriber subscriber = new VWAPSubscriber(new VWAPCalculator(), asserter);
        subscriber.onMessage(mde1);

    }

    @Test
    public void testTwoTicks() {
        MarketDataEvent mde1 = new MarketDataEvent(10110000, 1_000_000, Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        MarketDataEvent mde2 = new MarketDataEvent(11000000, 2_000_000, com.efx.vwap.models.Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        VWAPByInstrument result = new VWAPByInstrument(Instrument.GBPUSD);
        result.getBuy().setCumulativePriceAndQty(10110000000000L);
        result.getBuy().setCumulativeQuantity(1_000_000);
        result.getBuy().setVwap(10110000);
        VWAPPublisherAsserter asserter = new VWAPPublisherAsserter();
        asserter.setVwapByInstrument(result);
        VWAPSubscriber subscriber = new VWAPSubscriber(new VWAPCalculator(), asserter);
        subscriber.onMessage(mde1);

        result.getBuy().setCumulativePriceAndQty(32110000000000L);
        result.getBuy().setCumulativeQuantity(3_000_000);
        result.getBuy().setVwap(10703333);

        subscriber.onMessage(mde2);

    }

    @Test
    public void testTwoTicksDiffSides() {
        MarketDataEvent mde1 = new MarketDataEvent(10110000, 1_000_000, Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        MarketDataEvent mde2 = new MarketDataEvent(11000000, 2_000_000, Instrument.GBPUSD,
                Side.SELL, Venue.CURRENEX, System.currentTimeMillis());

        VWAPByInstrument result = new VWAPByInstrument(Instrument.GBPUSD);
        result.getBuy().setCumulativePriceAndQty(10110000000000L);
        result.getBuy().setCumulativeQuantity(1_000_000);
        result.getBuy().setVwap(10110000);
        VWAPPublisherAsserter asserter = new VWAPPublisherAsserter();
        asserter.setVwapByInstrument(result);
        VWAPSubscriber subscriber = new VWAPSubscriber(new VWAPCalculator(), asserter);
        subscriber.onMessage(mde1);

        result.getSell().setCumulativePriceAndQty(22000000000000L);
        result.getSell().setCumulativeQuantity(2_000_000);
        result.getSell().setVwap(11000000);

        subscriber.onMessage(mde2);

    }

    @Test
    public void testTwoTicksDiffVenues() {
        MarketDataEvent mde1 = new MarketDataEvent(10110000, 1_000_000, Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        MarketDataEvent mde2 = new MarketDataEvent(11000000, 2_000_000, com.efx.vwap.models.Instrument.GBPUSD,
                Side.BUY, Venue.CME_EBS, System.currentTimeMillis());

        VWAPByInstrument result = new VWAPByInstrument(Instrument.GBPUSD);
        result.getBuy().setCumulativePriceAndQty(10110000000000L);
        result.getBuy().setCumulativeQuantity(1_000_000);
        result.getBuy().setVwap(10110000);
        VWAPPublisherAsserter asserter = new VWAPPublisherAsserter();
        asserter.setVwapByInstrument(result);
        VWAPSubscriber subscriber = new VWAPSubscriber(new VWAPCalculator(), asserter);
        subscriber.onMessage(mde1);

        result.getBuy().setCumulativePriceAndQty(32110000000000L);
        result.getBuy().setCumulativeQuantity(3_000_000);
        result.getBuy().setVwap(10703333);

        subscriber.onMessage(mde2);

    }

    private static class VWAPPublisherAsserter implements Subscriber<VWAPByInstrument> {

        private VWAPByInstrument vwapByInstrument;

        public void setVwapByInstrument(final VWAPByInstrument vwapByInstrument) {
            this.vwapByInstrument = vwapByInstrument;
        }

        @Override
        public void onMessage(VWAPByInstrument result) {

            assertEquals(vwapByInstrument, result);

        }
    }
}
