package com.example.weatherhere;

import android.app.Application;

import com.example.weatherhere.mvp.models.Weather;
import com.example.weatherhere.sources.WeatherApi;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andrei on 07.04.2018.
 */

public class MyApp extends Application {

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static Retrofit retrofit;


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(WeatherApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
