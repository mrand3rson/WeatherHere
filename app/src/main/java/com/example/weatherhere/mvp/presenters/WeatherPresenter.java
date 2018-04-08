package com.example.weatherhere.mvp.presenters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.widget.ImageView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.weatherhere.MyApp;
import com.example.weatherhere.mvp.models.QueryInfo;
import com.example.weatherhere.mvp.views.WeatherView;
import com.example.weatherhere.sources.ImageViewSetter;
import com.example.weatherhere.sources.WeatherApi;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by Andrei on 08.04.2018.
 */
@InjectViewState
public class WeatherPresenter extends MvpPresenter<WeatherView> {
    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    private String mCityName;
    private Location mLocation;


    public void getWeather() {
        WeatherApi api = MyApp.getRetrofit().create(WeatherApi.class);
        api.getWeather(mCityName, WeatherApi.API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        long currentTime = Calendar.getInstance().getTime().getTime();
                        saveRequest(new QueryInfo(currentTime,
                                mLocation.getLatitude(),
                                mLocation.getLongitude(),
                                mCityName));
                        getViewState().retrieveWeather(response);
                    }
                });
    }

    public void setBitmapFromURL(String src, ImageViewSetter setter) {
        Single.fromCallable(() -> {
            URL url = new URL(src);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(bitmap -> {
                    Bitmap scaledBitmap =
                            Bitmap.createScaledBitmap(bitmap, 250, 250, false);
                    setter.set(scaledBitmap);
                });
    }

    private void saveRequest(QueryInfo item) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(item);
        realm.commitTransaction();
    }
}
