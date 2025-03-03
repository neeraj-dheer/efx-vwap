package com.efx.vwap.models;

import java.util.HashMap;
import java.util.Map;

public enum Instrument {

    GBPUSD,
    EURUSD;

    private static final Map<String, Instrument> instrumentMap = new HashMap<>();
    private static final Map<Instrument, char[]> instrumentCharMap = new HashMap<>();

    static {
        instrumentMap.put("GBP/USD", GBPUSD);
        instrumentMap.put("EUR/USD", EURUSD);

        instrumentCharMap.put(GBPUSD, "GBP/USD".toCharArray());
        instrumentCharMap.put(EURUSD, "EUR/USD".toCharArray());
    }

    public static Instrument getInstrument(final String instrument) {
        return instrument != null ? instrumentMap.get(instrument.toUpperCase()) : null;
    }

    public static char[] getInstrumentCharArray(final Instrument instrument) {
        return instrument != null ? instrumentCharMap.get(instrument) : null;
    }
}
