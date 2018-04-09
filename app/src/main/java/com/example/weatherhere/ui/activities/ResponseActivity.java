package com.example.weatherhere.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.weatherhere.R;
import com.example.weatherhere.mvp.models.QueryInfo;
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

import static com.example.weatherhere.sources.Constants.QUERY_INFO;

/**
 * Created by Andrei on 09.04.2018.
 */

public class ResponseActivity extends MvpAppCompatActivity
        implements WeatherView {

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

    @BindView(R.id.wind_speed)
    TextView mTextWindSpeedView;

    @BindView(R.id.temp_average)
    TextView mTextTemperatureView;

    @BindView(R.id.humidity)
    TextView mTextHumidityView;

    @BindView(R.id.pressure)
    TextView mTextPressureView;

    @InjectPresenter
    WeatherPresenter mPresenter;

    QueryInfo mQueryInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_weather);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            mQueryInfo = intent.getParcelableExtra(QUERY_INFO);
            mPresenter.getWeatherFromHistory(mQueryInfo);
        }
    }

    public void retrieveWeather(WeatherApiResponse response) {
        if (response.weather != null) {
            mTextCityInfoView.setText(String.format(Locale.getDefault(),
                    PATTERN_CITY,
                    mQueryInfo.city));

            final int imageViewSize = mPresenter.getImageDimension();

            for (Weather weather : response.weather) {
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textWeatherMainView = new TextView(this);
                textWeatherMainView.setLayoutParams(params);
                TextView textWeatherDescriptionView = new TextView(this);
                textWeatherDescriptionView.setLayoutParams(params);

                ImageView imageWeatherIconView = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        imageViewSize, imageViewSize);
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
