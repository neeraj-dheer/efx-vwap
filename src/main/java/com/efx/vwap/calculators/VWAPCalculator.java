package com.efx.vwap.calculators;

import com.efx.vwap.models.MarketDataEvent;
import com.efx.vwap.models.VWAPByInstrument;

public class VWAPCalculator implements Calculator<MarketDataEvent,
        VWAPByInstrument.VWAP, VWAPByInstrument.VWAP> {
    @Override
    public void calculate(MarketDataEvent marketDataEvent,
                          VWAPByInstrument.VWAP vwap, VWAPByInstrument.VWAP resultHolder) {

        resultHolder.setInstrument(vwap.getInstrument());
        resultHolder.setCumulativePriceAndQty(vwap.getCumulativePriceAndQty() +
                (marketDataEvent.getQuantity() * marketDataEvent.getPrice()));
        resultHolder.setCumulativeQuantity(vwap.getCumulativeQuantity() + marketDataEvent.getQuantity());
        resultHolder.setVwap(resultHolder.getCumulativePriceAndQty() / resultHolder.getCumulativeQuantity());
    }
}
