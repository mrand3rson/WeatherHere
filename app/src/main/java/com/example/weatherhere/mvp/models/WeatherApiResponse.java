package com.example.weatherhere.mvp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andrei on 08.04.2018.
 */

public class WeatherApiResponse {

    @SerializedName("weather")
    public List<Weather> weatherList;
    public Wind wind;
    public WeatherMetrics main;

    public long getTime() {
        return dt;
    }

    private long dt;
}
