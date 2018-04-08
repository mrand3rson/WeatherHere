package com.example.weatherhere.ui.fragments;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherhere.R;
import com.example.weatherhere.sources.Constants;
import com.example.weatherhere.ui.activities.MainActivity;
import com.example.weatherhere.ui.services.AddressIntentService;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CoordsFragment extends Fragment {

    @BindView(R.id.layout_progress_coords)
    View mProgressCoords;

    @BindView(R.id.text_coords)
    TextView mTextCoords;

    @BindView(R.id.layout_progress_address)
    View mProgressAddress;

    @BindView(R.id.text_address)
    TextView mTextAddress;

    @BindView(R.id.button_weather)
    Button mWeatherButton;

    private AddressResultReceiver mResultReceiver;
    private Location mLocation;


    public CoordsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResultReceiver = new AddressResultReceiver(new Handler());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coords, container, false);

        ButterKnife.bind(this, v);

        waitForCoords();

        return v;
    }

    public void setCoords(Location location) {
        toggleCoordsProgressVisibility(false);
        mLocation = location;
        mTextCoords.append(String.format(Locale.getDefault(),
                "%f : %f",
                location.getLatitude(), location.getLongitude()));

        //reverse geocoding here
        startIntentService(location);
    }

    protected void startIntentService(Location location) {
        toggleAddressProgressVisibility(true);
        Intent intent = new Intent(getActivity(), AddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_LAT, location.getLatitude());
        intent.putExtra(Constants.LOCATION_LON, location.getLongitude());
        getActivity().startService(intent);
    }

    private void waitForCoords() {
        mWeatherButton.setVisibility(View.GONE);
        toggleCoordsProgressVisibility(true);
        hideAddressSection();
    }

    private void hideAddressSection() {
        mProgressAddress.setVisibility(View.GONE);
        mTextAddress.setVisibility(View.GONE);
    }

    private void toggleCoordsProgressVisibility(boolean coords) {
        if (coords) {
            mProgressCoords.setVisibility(View.VISIBLE);
            mTextCoords.setVisibility(View.GONE);
        } else {
            mProgressCoords.setVisibility(View.GONE);
            mTextCoords.setVisibility(View.VISIBLE);
        }
    }

    private void toggleAddressProgressVisibility(boolean address) {
        if (address) {
            mProgressAddress.setVisibility(View.VISIBLE);
            mTextAddress.setVisibility(View.GONE);
        } else {
            mProgressAddress.setVisibility(View.GONE);
            mTextAddress.setVisibility(View.VISIBLE);
        }
    }

    public void onFinishGetAddress(final String address) {
        mTextAddress.append(address);
        toggleAddressProgressVisibility(false);
        mWeatherButton.setOnClickListener(view -> {
            ((MainActivity)getActivity()).setCurrentFragment(
                    WeatherFragment.newInstance(address, mLocation)
            );
        });
        mWeatherButton.setVisibility(View.VISIBLE);
    }


    class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }

            onFinishGetAddress(mAddressOutput);


            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(getActivity(), getString(R.string.address_found), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
