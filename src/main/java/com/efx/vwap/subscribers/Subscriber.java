package com.efx.vwap.subscribers;

public interface Subscriber<T> {

    void onMessage(T t);
}
