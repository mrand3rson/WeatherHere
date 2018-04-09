package com.example.weatherhere.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Andrei on 08.04.2018.
 */

public class QueryInfo extends RealmObject implements Parcelable {

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


    protected QueryInfo(Parcel in) {
        dateTime = in.readLong();
        lat = in.readDouble();
        lng = in.readDouble();
        city = in.readString();
    }

    public static final Creator<QueryInfo> CREATOR = new Creator<QueryInfo>() {
        @Override
        public QueryInfo createFromParcel(Parcel in) {
            return new QueryInfo(in);
        }

        @Override
        public QueryInfo[] newArray(int size) {
            return new QueryInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(dateTime);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(city);
    }
}
