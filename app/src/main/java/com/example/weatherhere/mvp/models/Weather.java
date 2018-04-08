package com.example.weatherhere.mvp.models;

/**
 * Created by Andrei on 07.04.2018.
 */

public class Weather {

    public int getId() {
        return id;
    }

    private int id;
    public String main;
    public String description;
    public String icon;
}