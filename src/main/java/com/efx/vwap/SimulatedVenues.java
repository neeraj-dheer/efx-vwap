package com.efx.vwap;

import com.efx.vwap.models.Instrument;
import com.efx.vwap.models.MarketDataEvent;
import com.efx.vwap.models.Side;
import com.efx.vwap.models.Venue;
import com.efx.vwap.subscribers.SubscriptionManager;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulatedVenues {

    private final int SIMULATED_VENUE_THREADS = 1;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Random random = new Random();
    private final Venue[] venues = Venue.values();
    private final Instrument[] instruments = Instrument.values();
    private final Side[] sides = Side.values();

    private final SubscriptionManager<MarketDataEvent> subscriptionManager;
    private final ThreadLocal<MarketDataEvent> marketDataEventThreadLocal = ThreadLocal.withInitial(MarketDataEvent::new);

    public SimulatedVenues(final SubscriptionManager<MarketDataEvent> subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
        scheduledExecutorService = Executors.newScheduledThreadPool(SIMULATED_VENUE_THREADS);
    }

    public void start() {

        //send 1 tick/second
        scheduledExecutorService.scheduleWithFixedDelay(
                this::sendTick, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduledExecutorService.shutdownNow();
    }

    private void sendTick() {
        MarketDataEvent mde = getTick();
        subscriptionManager.onMessage(mde.getVenue(), mde.getInstrument(), mde);
    }

    /**
     * using a threadlocal tick - assumption is that this tick is only mutated
     * in this class and each thread finishes its work with this tick before reading
     * another tick. This thread might:
     * 1. Hand off the tick to subcriptionmanager over a Disruptor
     * 2. Go all the way to the subscriber (like now)
     * but in either case does not process another tick until this tick is processed.
     *
     * @return tick
     */
    private MarketDataEvent getTick() {
        MarketDataEvent mde = marketDataEventThreadLocal.get();
        mde.setVenue(venues[random.nextInt(venues.length)]);
        mde.setInstrument(instruments[random.nextInt(instruments.length)]);
        mde.setSide(sides[random.nextInt(sides.length)]);
        mde.setQuantity(random.nextInt(2_000_000));
        mde.setPrice(random.nextLong(15_000_000));
        mde.setTimestampms(System.currentTimeMillis());

        return mde;
    }
}
