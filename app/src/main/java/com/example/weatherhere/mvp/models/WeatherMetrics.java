package com.example.weatherhere.mvp.models;

import com.example.weatherhere.mvp.models.metrics.Pressure;
import com.example.weatherhere.mvp.models.metrics.Temperature;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrei on 08.04.2018.
 */

public class WeatherMetrics {

    public Temperature getTemperature() {
        return new Temperature(temperature);
    }

    public Temperature getMinTemperature() {
        return new Temperature(minTemperature);
    }

    public Temperature getMaxTemerature() {
        return new Temperature(maxTemerature);
    }

    public Pressure getPressure() {
        return new Pressure(pressure);
    }

    @SerializedName("temp")
    private double temperature;

    @SerializedName("temp_min")
    private double minTemperature;

    @SerializedName("temp_max")
    private double maxTemerature;

    public double humidity;
    private double pressure;
}