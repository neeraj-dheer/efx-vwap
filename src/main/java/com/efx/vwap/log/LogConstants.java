package com.efx.vwap.log;

import java.util.Arrays;

public class LogConstants {

    public static final char[] INVALID_INSTRUMENT   = Arrays.copyOf("Invalid Instrument".toCharArray(), 32);
    public static final char[] NULL_INSTRUMENT      = "Null Instrument".toCharArray();
    public static final char[] NEGATIVE_QUANTITY    = "Negative Quantity".toCharArray();
    public static final char[] NEGATIVE_PRICE       = "Negative Price".toCharArray();

}
