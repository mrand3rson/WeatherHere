package com.example.weatherhere.mvp.views;

import android.graphics.Bitmap;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.weatherhere.mvp.models.WeatherApiResponse;

/**
 * Created by Andrei on 08.04.2018.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherView extends MvpView {
    void retrieveWeather(WeatherApiResponse response);
}
