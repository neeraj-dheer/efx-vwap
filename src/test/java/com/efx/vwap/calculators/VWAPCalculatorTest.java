package com.efx.vwap.calculators;

import com.efx.vwap.models.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VWAPCalculatorTest {

    @Test
    public void testOneTick() {

        MarketDataEvent mde = new MarketDataEvent(10110000, 1_000_000, com.efx.vwap.models.Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        VWAPByInstrument fullVWAP = new VWAPByInstrument(Instrument.GBPUSD);

        VWAPByInstrument.VWAP result = new VWAPByInstrument.VWAP();
        VWAPCalculator calculator = new VWAPCalculator();

        calculator.calculate(mde, fullVWAP.getBuy(), result);

        assertVwap(10110000000000L, 1_000_000L, 10110000L, result);
    }

    @Test
    public void testTwoTicks() {

        MarketDataEvent mde1 = new MarketDataEvent(10110000, 1_000_000, com.efx.vwap.models.Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        MarketDataEvent mde2 = new MarketDataEvent(11000000, 2_000_000, com.efx.vwap.models.Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        VWAPByInstrument fullVWAP = new VWAPByInstrument(Instrument.GBPUSD);

        VWAPByInstrument.VWAP result = new VWAPByInstrument.VWAP();
        VWAPCalculator calculator = new VWAPCalculator();

        calculator.calculate(mde1, fullVWAP.getBuy(), result);

        assertVwap(10110000000000L, 1_000_000L, 10110000L, result);
        fullVWAP.getBuy().copy(result);
        calculator.calculate(mde2, fullVWAP.getBuy(), result);

        assertVwap(32110000000000L, 3_000_000L, 10703333L, result);
    }

    @Test
    public void testTwoTicksDiffSides() {


        MarketDataEvent mde1 = new MarketDataEvent(10110000, 1_000_000, com.efx.vwap.models.Instrument.GBPUSD,
                Side.BUY, Venue.CURRENEX, System.currentTimeMillis());

        MarketDataEvent mde2 = new MarketDataEvent(11000000, 2_000_000, com.efx.vwap.models.Instrument.GBPUSD,
                Side.SELL, Venue.CURRENEX, System.currentTimeMillis());

        VWAPByInstrument fullVWAP = new VWAPByInstrument(Instrument.GBPUSD);

        VWAPByInstrument.VWAP result = new VWAPByInstrument.VWAP();
        VWAPCalculator calculator = new VWAPCalculator();

        calculator.calculate(mde1, fullVWAP.getBuy(), result);

        assertVwap(10110000000000L, 1_000_000L, 10110000L, result);

        fullVWAP.getBuy().copy(result);
        calculator.calculate(mde2, fullVWAP.getSell(), result);

        assertVwap(10110000000000L, 1_000_000L, 10110000L, fullVWAP.getBuy());


    }

    private void assertVwap(long cumulativePriceAndQty, long cumulativeQty, long vwap, VWAPByInstrument.VWAP result) {
        assertEquals(cumulativePriceAndQty, result.getCumulativePriceAndQty());
        assertEquals(cumulativeQty, result.getCumulativeQuantity());
        assertEquals(vwap, result.getVwap());

    }
}
