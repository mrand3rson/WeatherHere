package com.example.weatherhere.mvp.models.metrics;

/**
 * Created by Andrei on 08.04.2018.
 */

public class Temperature {

    public double inC() {
        return C;
    }

    public double inF() {
        return F;
    }

    private final double C;
    private final double F;


    public Temperature(double tempK) {
        this.C = tempK - 273;
        this.F = (C+32)*9/5;
    }
}
