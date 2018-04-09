package com.example.weatherhere.sources;

import com.example.weatherhere.mvp.models.WeatherApiResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andrei on 08.04.2018.
 */

public interface WeatherApi {
    String API_KEY = "460645603e0799fc46cbe6aebd8f62e4";
    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @GET("weather")
    Observable<WeatherApiResponse> getWeather(@Query("q") String cityName,
                                              @Query("APPID") String apiKey);

    @GET("weather")
    Observable<WeatherApiResponse> getWeather(@Query("lat") double lat,
                                              @Query("lon") double lon,
                                              @Query("APPID") String apiKey);
}
