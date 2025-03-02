package com.efx.vwap.calculators;

/**
 * Calculate based on input and current data.
 * Input typically will be a tick (but does not need to be).
 * This returns the result in the result-holder `R` rather than
 * creating a new object(s) for results every time. Hence expects the
 * result-holder provided to be mutable.
 * This should NOT mutate any of the two inputs.
 *
 * @param <T> - input
 * @param <U> - data
 * @param <R> - result holder
 */
public interface Calculator<T, U, R> {

    void calculate(T t, U u, R r);
}
