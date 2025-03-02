package com.efx.vwap.subscribers;


import com.efx.vwap.models.Instrument;
import com.efx.vwap.models.Venue;

public interface SubscriptionManager<T> {
    void subscribe(Venue venue, Instrument instrument, Subscriber<T> subscriber);

    void unsubscribe(Venue venue, Instrument instrument, Subscriber<T> subscriber);

    void onMessage(Venue venue, Instrument instrument, T t);

}
