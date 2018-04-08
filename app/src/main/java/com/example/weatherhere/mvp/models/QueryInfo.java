package com.example.weatherhere.mvp.models;

import io.realm.RealmObject;

/**
 * Created by Andrei on 08.04.2018.
 */

public class QueryInfo extends RealmObject {

    public long dateTime;
    public double lat;
    public double lng;
    public String city;


    public QueryInfo() {

    }

    public QueryInfo(long dateTime, double lat, double lng, String city) {
        this.dateTime = dateTime;
        this.lat = lat;
        this.lng = lng;
        this.city = city;
    }
}
