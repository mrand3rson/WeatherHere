package com.example.weatherhere.mvp.models.metrics;

/**
 * Created by Andrei on 08.04.2018.
 */

public class Pressure {

    public double inMmHg() {
        return mmHg;
    }

    public double inPascal() {
        return pascal;
    }

    private double mmHg;
    private double pascal;


    public Pressure(double pascal) {
        this.pascal = pascal;
        this.mmHg = pascal * 101.325 / 760;
    }
}
