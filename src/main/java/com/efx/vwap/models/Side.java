package com.efx.vwap.models;

import java.util.HashMap;
import java.util.Map;

public enum Side {

    SELL,
    BUY;

    private static final Map<String, Side> sideMap = new HashMap<>();

    static {
        sideMap.put("SELL", SELL);
        sideMap.put("BUY", BUY);
    }

    public static Side getSide(final String side) {
        return side != null ? sideMap.get(side.toUpperCase()) : null;
    }

}
