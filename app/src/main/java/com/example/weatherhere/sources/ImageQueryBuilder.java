package com.example.weatherhere.sources;

/**
 * Created by Andrei on 08.04.2018.
 */

public class ImageQueryBuilder {
    private final static String BASE_URL = "http://openweathermap.org/img/w/";


    public static String build(String param) {
        return BASE_URL.concat(param).concat(".png");
    }
}
