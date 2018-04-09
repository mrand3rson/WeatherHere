package com.example.weatherhere.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.weatherhere.R;
import com.example.weatherhere.ui.fragments.CoordsFragment;
import com.example.weatherhere.ui.fragments.WeatherFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static int CODE_GET_PERMISSION = 1;
    public final static String TAG_COORDS_FRAGMENT = "coords";
    public final static String TAG_WEATHER_FRAGMENT = "weather";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private CoordsFragment mCoordsFragment;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }

                    mCoordsFragment.setLocation(locationResult.getLastLocation());
                    mCoordsFragment.showCoords();
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }
            };
        } else {
            WeatherFragment weatherFragment = (WeatherFragment)
                    getSupportFragmentManager().findFragmentByTag(TAG_WEATHER_FRAGMENT);
            if (weatherFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, weatherFragment)
                        .commit();
            } else {

                mCoordsFragment = (CoordsFragment)
                        getSupportFragmentManager().findFragmentByTag(TAG_COORDS_FRAGMENT);
                if (mCoordsFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, mCoordsFragment)
                            .commit();
                }
            }

            return;
        }

        mCoordsFragment = new CoordsFragment();
        addAndReplaceCoordsFragment(mCoordsFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCoordsFragment != null && mCoordsFragment.getLocation() == null) {
            if (mGoogleApiClient != null && mFusedLocationClient != null) {
                startLocationUpdates();
            } else {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                CODE_GET_PERMISSION);
                    }
                } else {
                    buildGoogleApiClient();
                }
            }
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "CONNEctioN FAILED", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        createLocationRequest();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                Looper.myLooper());
    }

    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GET_PERMISSION) {
            if (grantResults.length == 2
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        || grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    ) {
                startLocationUpdates();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void addAndReplaceCoordsFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(fragment, TAG_COORDS_FRAGMENT)
                .replace(R.id.container, fragment)
                .commit();
    }

    public void addAndReplaceWeatherFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(TAG_COORDS_FRAGMENT)
                .add(fragment, TAG_WEATHER_FRAGMENT)
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_stats) {
            Intent intent = new Intent(this, StatsActivity.class);
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                overridePendingTransition(R.anim.enter_right_in, R.anim.exit_left_out);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        super.onPause();
    }
}
