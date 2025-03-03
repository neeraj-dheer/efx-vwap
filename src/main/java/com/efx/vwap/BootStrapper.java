package com.efx.vwap;

import com.efx.vwap.models.Instrument;
import com.efx.vwap.models.MarketDataEvent;
import com.efx.vwap.models.Venue;
import com.efx.vwap.subscribers.DirectSubscriptionManager;
import com.efx.vwap.subscribers.SubscriptionManager;
import com.efx.vwap.subscribers.VWAPSubscriber;

public class BootStrapper {

    private SimulatedVenues simulatedVenues = null;
    private SubscriptionManager<MarketDataEvent> subscriptionManager = null;
    private VWAPSubscriber vwapSubscriber = null;

    public void start() {

        try {
            subscriptionManager = new DirectSubscriptionManager<>();
            vwapSubscriber = new VWAPSubscriber();

            //all of this to be derived from config
            subscriptionManager.subscribe(Venue.CURRENEX, Instrument.GBPUSD, vwapSubscriber);
            subscriptionManager.subscribe(Venue.CME_EBS, Instrument.GBPUSD, vwapSubscriber);
            subscriptionManager.subscribe(Venue.CME_EBS, Instrument.EURUSD, vwapSubscriber);

            simulatedVenues = new SimulatedVenues(subscriptionManager);

            //We should have initialized everything by now - including any cache warmup
            // so can run gc now and promote objects here itself and free up young gen
            //assuming java wants to honour our hint to run gc
            System.gc();
            simulatedVenues.start();
            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        } catch (Exception e) {
            stop();
        }

    }
        public void stop() {
        if((subscriptionManager != null) && (vwapSubscriber != null)) {
            subscriptionManager.unsubscribe(Venue.CURRENEX, Instrument.GBPUSD, vwapSubscriber);
            subscriptionManager.unsubscribe(Venue.CME_EBS, Instrument.GBPUSD, vwapSubscriber);
            subscriptionManager.unsubscribe(Venue.CME_EBS, Instrument.EURUSD, vwapSubscriber);
        }

        if(simulatedVenues != null) {
            simulatedVenues.stop();
        }

    }

    public static void main(String[] a) {
        BootStrapper bootStrapper = new BootStrapper();
        bootStrapper.start();
    }


}
