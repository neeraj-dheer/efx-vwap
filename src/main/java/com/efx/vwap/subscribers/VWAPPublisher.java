package com.efx.vwap.subscribers;

import com.efx.vwap.models.VWAPByInstrument;

import java.io.PrintStream;

import static com.efx.vwap.models.MarketDataEvent.PRICE_MULTIPLIER;

public class VWAPPublisher implements Subscriber<VWAPByInstrument> {

    private final String format = "{instrument=%s, buy={cumulativePriceAndQty=%f, cumulativeQty=%d, vwap=%f},"
            + "{sell={cumulativePriceAndQty=%f, cumulativeQty=%d, vwap=%f}}%n";
    private final PrintStream printStream;
    private final double multiplier;

    public VWAPPublisher() {

        this(System.out);
    }

    public VWAPPublisher(final PrintStream printStream) {
        this.printStream = printStream;
        multiplier = Math.pow(10, -PRICE_MULTIPLIER);
    }

    @Override
    public void onMessage(VWAPByInstrument vwapByInstrument) {
        VWAPByInstrument.VWAP buy = vwapByInstrument.getBuy();
        VWAPByInstrument.VWAP sell = vwapByInstrument.getSell();
        printStream.printf(format, vwapByInstrument.getInstrument(),
                buy.getCumulativePriceAndQty() * multiplier, buy.getCumulativeQuantity(), buy.getVwap() * multiplier,
                sell.getCumulativePriceAndQty() * multiplier, sell.getCumulativeQuantity(), sell.getVwap() * multiplier);
    }
}
