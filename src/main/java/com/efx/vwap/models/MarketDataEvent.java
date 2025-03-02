package com.efx.vwap.models;

import java.util.Objects;

public class MarketDataEvent {

    public final static int PRICE_MULTIPLIER = 7;

    //multiplier defined above
    private long price;

    //quantity is considered to be full always and hence does not have a scale multiplier
    private long quantity;
    private Instrument instrument;
    private Side side;
    private Venue venue;
    private long timestampms;

    public MarketDataEvent(long price, long quantity, Instrument instrument, Side side, Venue venue, long timestampms) {
        this.price = price;
        this.quantity = quantity;
        this.instrument = instrument;
        this.side = side;
        this.venue = venue;
        this.timestampms = timestampms;
    }

    public MarketDataEvent() {

    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public long getTimestampms() {
        return timestampms;
    }

    public void setTimestampms(long timestampms) {
        this.timestampms = timestampms;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        return "MarketDataEvent{" +
                ", price=" + price +
                ", remaining=" + quantity +
                ", instrument=" + instrument +
                ", side=" + side +
                ", venue='" + venue + '\'' +
                ", timestampms=" + timestampms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketDataEvent that = (MarketDataEvent) o;
        return Double.compare(that.price, price) == 0
                && Double.compare(that.quantity, quantity) == 0
                && instrument == that.instrument
                && side == that.side
                && venue == that.venue;

    }

    @Override
    public int hashCode() {
        return Objects.hash(price, quantity, instrument, side, venue);
    }

}
