package com.example.adrian.projekt2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

    String mLastUpdateTime;
    String mLatitude;
    String mLongitude;

    TextView latituteField;
    TextView longitudeField;
    TextView dateField;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_show = (Button) findViewById(R.id.button_showme);
        Button button_save = (Button) findViewById(R.id.button_savelocation);
        TextView userInfo = (TextView) findViewById(R.id.userInfo);

        latituteField = (TextView) findViewById(R.id.latituteField);
        longitudeField = (TextView) findViewById(R.id.longitudeField);
        dateField = (TextView) findViewById(R.id.dateField);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        userInfo.setText("Witaj, " + PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("userName", "null"));
        try {
            latituteField.setText((PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("latitude", "Brak zapisanych danych")).substring(0,5));
            longitudeField.setText((PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("longitude", "Brak zapisanych danych")).substring(0,5));
        } catch (Exception e) {
            latituteField.setText(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("latitude", "Brak zapisanych danych"));
            longitudeField.setText(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("longitude", "Brak zapisanych danych"));
        }

        dateField.setText("Data zapisu: " + PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("lastUpdateTime", "Brak zapisanych danych"));


        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                putIntoDb();
            }
        });
    }

    private void putIntoDb() {
        if (null != mCurrentLocation) {
            mLatitude = String.valueOf(mCurrentLocation.getLatitude());
            mLongitude = String.valueOf(mCurrentLocation.getLongitude());

            myDialog dialog = new myDialog();
            dialog.dialog_confirm(
                    this,
                    "Czy chcesz dodać tą lokalizację jako ulubioną?",
                    "Następujące dane zostaną dodane:",
                    mLatitude,
                    mLongitude,
                    mLastUpdateTime,
                    MainActivity.this
            );
        } else {
            Toast.makeText(this.getApplicationContext(), "Brak połączenia lub lokacja niezmieniona", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getDateTimeInstance().format(new Date());
        // putIntoDb();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }
}
