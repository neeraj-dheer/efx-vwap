package com.efx.vwap.models;

import java.util.Objects;

public class VWAPByInstrument {

    private final Instrument instrument;
    private final VWAP buy;
    private final VWAP sell;

    public VWAPByInstrument(final Instrument instrument) {
        this.instrument = instrument;
        buy = new VWAP();
        buy.setInstrument(instrument);
        sell = new VWAP();
        sell.setInstrument(instrument);
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public VWAP getBuy() {
        return buy;
    }

    public VWAP getSell() {
        return sell;
    }

    @Override
    public String toString() {
        return "VWAPByInstrument{" +
                "instrument=" + instrument +
                ", buy=" + buy +
                ", sell=" + sell +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VWAPByInstrument that = (VWAPByInstrument) o;
        return instrument == that.instrument && Objects.equals(buy, that.buy) && Objects.equals(sell, that.sell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, buy, sell);
    }

    public static class VWAP {
        private Instrument instrument;
        private long cumulativePriceAndQty;
        private long cumulativeQuantity;
        private long vwap;

        public Instrument getInstrument() {
            return instrument;
        }

        public void setInstrument(Instrument instrument) {
            this.instrument = instrument;
        }

        public long getCumulativePriceAndQty() {
            return cumulativePriceAndQty;
        }

        public void setCumulativePriceAndQty(long cumulativePriceAndQty) {
            this.cumulativePriceAndQty = cumulativePriceAndQty;
        }

        public long getCumulativeQuantity() {
            return cumulativeQuantity;
        }

        public void setCumulativeQuantity(long cumulativeQuantity) {
            this.cumulativeQuantity = cumulativeQuantity;
        }

        public long getVwap() {
            return vwap;
        }

        public void setVwap(long vwap) {
            this.vwap = vwap;
        }

        public void copy(final VWAP that) {
            instrument = that.getInstrument();
            cumulativeQuantity = that.getCumulativeQuantity();
            cumulativePriceAndQty = that.getCumulativePriceAndQty();
            vwap = that.getVwap();
        }

        @Override
        public String toString() {
            return "VWAP{" +
                    "instrument=" + instrument +
                    ", cumulativePriceAndQty=" + cumulativePriceAndQty +
                    ", cumulativeQuantity=" + cumulativeQuantity +
                    ", vwap=" + vwap +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VWAP vwap1 = (VWAP) o;
            return cumulativePriceAndQty == vwap1.cumulativePriceAndQty
                    && cumulativeQuantity == vwap1.cumulativeQuantity
                    && vwap == vwap1.vwap
                    && instrument == vwap1.instrument;
        }

        @Override
        public int hashCode() {
            return Objects.hash(instrument, cumulativePriceAndQty, cumulativeQuantity, vwap);
        }
    }
}
