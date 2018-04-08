package com.example.weatherhere.ui.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.example.weatherhere.sources.Constants;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Result;


public class AddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;


    public AddressIntentService() {
        super("AddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (Geocoder.isPresent() && intent != null) {

            Geocoder geocoder = new Geocoder(this);
            mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            double lat = intent.getDoubleExtra(Constants.LOCATION_LAT, 0);
            double lon = intent.getDoubleExtra(Constants.LOCATION_LON, 0);
            try {
                List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
                //example: coordinates in Minsk
                //addressList = geocoder.getFromLocation(53.92506, 27.5941, 1);

                //returns the city name
                deliverResultToReceiver(Constants.SUCCESS_RESULT, addressList.get(0).getLocality());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
