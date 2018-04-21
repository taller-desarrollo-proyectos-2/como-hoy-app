package com.fiuba.gaff.comohoy;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MoreInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 10;
    private static final String LOG_TAG = "MoreInfoActivity";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_restaurant);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = googleAPI.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            dialog.show();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    public LatLng getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                Log.e(LOG_TAG, "Error getting location from " + strAddress);
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng((location.getLatitude() * 1E6),
                    (location.getLongitude() * 1E6));
        }
        catch (IOException ex) {
            Log.e(LOG_TAG, "Error getting location from " + strAddress + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        return p1;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        //LatLng location = null;
        //try {
        //    location = getLocationFromAddress("Avenida Paseo Colón, Buenos Aires 850");
        //}catch(IOException ex) {
            //Do something with the exception
        //}

        LatLng location = new LatLng(-34.617290, -58.367846); //PACEO COLON 850
        mMap.addMarker(new MarkerOptions().position(location).title("Mi item_comercio0").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        float zoomLevel = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));
    }
}
