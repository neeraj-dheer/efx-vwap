package com.efx.vwap.subscribers;

import com.efx.vwap.models.Instrument;
import com.efx.vwap.models.Venue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SubscriptionManager that calls the subscribers in the same thread as the caller/event source.
 * Calls each subscriber for that particular instrument serially.
 * This class is intended to be thread-safe since multiple event sources might call this manager on events.
 * (A race condition does exist if the same subscriber is subscribed to more than one instrument and those instruments
 * provide updates from different threads)
 * <p>
 * Typically this event distribution will be done over a thread hand-off (executor/disruptor) or a message bus (aeron etc)
 * and this specific implementation is for testing and show-casing design only.
 *
 * @param <T> - type of market data event
 */

public class DirectSubscriptionManager<T> implements SubscriptionManager<T> {

    private final Map<Venue, Map<Instrument, Collection<Subscriber<T>>>> subscribers = new ConcurrentHashMap<>();
    private final Map<Instrument, Collection<Subscriber<T>>> EMPTY_SUBSCRIBERS = new HashMap<>();
    private final Collection<Subscriber<T>> EMPTY_LIST = new ArrayList<>();

    @Override
    public void subscribe(final Venue venue, final Instrument instrument, final Subscriber<T> subscriber) {
        subscribers.computeIfAbsent(venue, key -> new ConcurrentHashMap<>())
                .computeIfAbsent(instrument, key -> new ArrayList<>())
                .add(subscriber);
    }

    public void unsubscribe(final Venue venue, final Instrument instrument, final Subscriber<T> subscriber) {

        //not worrying about the actual subscriber not having subscribed to that particular instrument
        //or the instrument no longer having any subscribers and so just has an empty list of subscribers
        //since expect these operations to be infrequent.
        subscribers.getOrDefault(venue, EMPTY_SUBSCRIBERS)
                .computeIfPresent(instrument, (ins, insSubscribers) -> {
                    insSubscribers.remove(subscriber);
                    return insSubscribers;
                });
    }

    @Override
    public void onMessage(final Venue venue, final Instrument instrument, T o) {
        subscribers.getOrDefault(venue, EMPTY_SUBSCRIBERS)
                .getOrDefault(instrument, EMPTY_LIST).forEach(subscriber -> subscriber.onMessage(o));

    }
}
