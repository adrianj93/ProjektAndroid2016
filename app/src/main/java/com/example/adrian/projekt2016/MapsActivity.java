package com.example.adrian.projekt2016;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private FloatingActionButton fab;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = 0;
        double lang = 0;

        try {
            lat = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(MapsActivity.this).getString("latitude", "Brak zapisanych danych"));
            lang = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(MapsActivity.this).getString("longitude", "Brak zapisanych danych"));
        } catch (Exception e) {
            Toast.makeText(MapsActivity.this, "Ustaw najpierw lokalizację", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        LatLng miejsce = new LatLng(lat, lang);
        mMap.addMarker(new MarkerOptions().position(miejsce).title("To Twoje miejsce"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miejsce));

        final Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);

        fab.setVisibility(View.VISIBLE);
        fab.startAnimation(mAnimation);
    }
}
