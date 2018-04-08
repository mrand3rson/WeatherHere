package com.example.weatherhere.ui.fragments;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.weatherhere.R;
import com.example.weatherhere.mvp.models.Weather;
import com.example.weatherhere.mvp.models.WeatherApiResponse;
import com.example.weatherhere.mvp.models.metrics.Pressure;
import com.example.weatherhere.mvp.models.metrics.Temperature;
import com.example.weatherhere.mvp.presenters.WeatherPresenter;
import com.example.weatherhere.mvp.views.WeatherView;
import com.example.weatherhere.sources.ImageQueryBuilder;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherFragment extends MvpAppCompatFragment implements WeatherView {

    private static final String CITY_NAME = "CITY_NAME";
    private static final String LOCATION = "LOCATION";

    @BindString(R.string.pattern_city)
    String PATTERN_CITY;

    @BindString(R.string.pattern_temp_av)
    String PATTERN_TEMP_AV;

    @BindString(R.string.pattern_pressure)
    String PATTERN_PRESSURE;

    @BindString(R.string.pattern_humidity)
    String PATTERN_HUMIDITY;

    @BindString(R.string.pattern_wind_speed)
    String PATTERN_WIND_SPEED;


    @BindView(R.id.progress)
    ProgressBar mProgress;

    @BindView(R.id.city_info)
    TextView mTextCityInfoView;

    @BindView(R.id.weather_info)
    LinearLayout mWeatherInfo;

    @BindView(R.id.wind_info)
    LinearLayout mWindInfo;

    @BindView(R.id.wind_speed)
    TextView mTextWindSpeedView;

    @BindView(R.id.main_info)
    LinearLayout mTemperatureInfo;

    @BindView(R.id.temp_average)
    TextView mTextTemperatureView;

    @BindView(R.id.humidity)
    TextView mTextHumidityView;

    @BindView(R.id.pressure)
    TextView mTextPressureView;

    @InjectPresenter
    WeatherPresenter mPresenter;

    private String mCityName;
    private Location mLocation;


    public WeatherFragment() {

    }

    public static WeatherFragment newInstance(String cityName, Location location) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        args.putParcelable(LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCityName = getArguments().getString(CITY_NAME);
            mLocation = getArguments().getParcelable(LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, v);
        mWeatherInfo.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.setCityName(mCityName);
        mPresenter.setLocation(mLocation);
        mPresenter.getWeather();
    }

    public void retrieveWeather(WeatherApiResponse response) {
        if (response.weatherList != null) {
            mTextCityInfoView.setText(String.format(Locale.getDefault(),
                    PATTERN_CITY,
                    mCityName));
            for (Weather weather : response.weatherList) {
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textWeatherMainView = new TextView(getActivity());
                textWeatherMainView.setLayoutParams(params);
                TextView textWeatherDescriptionView = new TextView(getActivity());
                textWeatherDescriptionView.setLayoutParams(params);

                ImageView imageWeatherIconView = new ImageView(getActivity());
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageParams.gravity = Gravity.CENTER;
                imageWeatherIconView.setLayoutParams(imageParams);

                textWeatherMainView.setText(weather.main);
                textWeatherDescriptionView.setText(weather.description);
                mPresenter.setBitmapFromURL(ImageQueryBuilder.build(weather.icon),
                        imageWeatherIconView::setImageBitmap);

                mWeatherInfo.addView(imageWeatherIconView);
                mWeatherInfo.addView(textWeatherMainView);
                mWeatherInfo.addView(textWeatherDescriptionView);
            }
        }

        if (response.main != null) {
            Temperature t = response.main.getTemperature();
            mTextTemperatureView.setText(String.format(Locale.getDefault(),
                    PATTERN_TEMP_AV, t.inC(), t.inF()));

            Pressure p = response.main.getPressure();
            mTextPressureView.setText(String.format(Locale.getDefault(),
                    PATTERN_PRESSURE, p.inMmHg(), p.inPascal()));
            mTextHumidityView.setText(String.format(Locale.getDefault(),
                    PATTERN_HUMIDITY, response.main.humidity));
        }

        if (response.wind != null) {
            mTextWindSpeedView.setText(String.format(Locale.getDefault(),
                    PATTERN_WIND_SPEED, response.wind.speed));
        }

        toggleWeatherProgressVisibility(false);
    }

    private void toggleWeatherProgressVisibility(boolean toggle) {
        if (toggle) {
            mProgress.setVisibility(View.VISIBLE);
            mWeatherInfo.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.GONE);
            mWeatherInfo.setVisibility(View.VISIBLE);
        }
    }
}
