package com.example.weatherhere.mvp.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Andrei on 08.04.2018.
 */

public class WeatherApiResponse extends RealmObject{


    public RealmList<Weather> weather;
    public Wind wind;
    public WeatherMetrics main;
    public long dt;


    public WeatherApiResponse() {

    }
}
