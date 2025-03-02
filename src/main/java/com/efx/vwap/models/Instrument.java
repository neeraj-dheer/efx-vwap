package com.efx.vwap.models;

import java.util.HashMap;
import java.util.Map;

public enum Instrument {

    GBPUSD,
    EURUSD;

    private static final Map<String, Instrument> instrumentMap = new HashMap<>();

    static {
        instrumentMap.put("GBP/USD", GBPUSD);
        instrumentMap.put("EUR/USD", EURUSD);
    }

    public static Instrument getInstrument(final String instrument) {
        return instrument != null ? instrumentMap.get(instrument.toUpperCase()) : null;
    }
}
