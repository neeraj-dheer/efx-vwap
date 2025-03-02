package com.efx.vwap;

import com.efx.vwap.models.Instrument;
import com.efx.vwap.models.MarketDataEvent;
import com.efx.vwap.models.Venue;
import com.efx.vwap.subscribers.DirectSubscriptionManager;
import com.efx.vwap.subscribers.SubscriptionManager;
import com.efx.vwap.subscribers.VWAPSubscriber;

public class BootStrapper {

    public static void main(String[] a) throws Exception {

        SubscriptionManager<MarketDataEvent> subscriptionManager = new DirectSubscriptionManager<>();
        VWAPSubscriber vwapSubscriber = new VWAPSubscriber();

        subscriptionManager.subscribe(Venue.CURRENEX, Instrument.GBPUSD, vwapSubscriber);
        subscriptionManager.subscribe(Venue.CME_EBS, Instrument.GBPUSD, vwapSubscriber);
        subscriptionManager.subscribe(Venue.CME_EBS, Instrument.EURUSD, vwapSubscriber);
        SimulatedVenues simulatedVenues = new SimulatedVenues(subscriptionManager);

        //We should have initialized everything by now - including any cache warmup
        // so can run gc now and promote objects here itself and free up young gen
        //assuming java wants to honour our hint to run gc
        System.gc();
        simulatedVenues.start();
        Runtime.getRuntime().addShutdownHook(new Thread(simulatedVenues::stop));

        Thread.sleep(10_000);

    }
}
