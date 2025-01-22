package it.BeGear.E_commerce.Dto;

public enum FasciaDiPrezzo {
    BASSA(0, 50),
    MEDIA(50, 200),
    ALTA(200, Integer.MAX_VALUE);

    private final double min;
    private final double max;

    FasciaDiPrezzo(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
