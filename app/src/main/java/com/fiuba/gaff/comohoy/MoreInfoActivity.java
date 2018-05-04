package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.OpeningTime;
import com.fiuba.gaff.comohoy.model.TimeInterval;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MoreInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 10;
    private static final String LOG_TAG = "MoreInfoActivity";

    private int mCommerceId = -1;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        obtainCommerceId(savedInstanceState);

        createOpeningTimesView();

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

    private void obtainCommerceId(Bundle savedInstanceState) {
        if (mCommerceId == -1) {
            Bundle extras = getIntent().getExtras();
            mCommerceId = extras.getInt(getString(R.string.intent_data_commerce_id), -1);
        }
        if ((mCommerceId == -1) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_commerce_id)))) {
            mCommerceId = savedInstanceState.getInt(getString(R.string.intent_data_commerce_id));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        double latitud = commerce.getLocation().getLatitud();
        double longitud = commerce.getLocation().getLongitud();
        LatLng location = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(location).title(commerce.getShowableName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        float zoomLevel = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));
    }

    private void createOpeningTimesView() {
        ViewGroup parentLayout = findViewById(R.id.layout_horarios);
        Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        List<OpeningTime> openingTimes = commerce.getOpeningTimes();
        for (OpeningTime openingTime : openingTimes) {
            View openingTimeView = createOpeningTimeView(openingTime);
            parentLayout.addView(openingTimeView);
        }
    }

    private View createOpeningTimeView(OpeningTime openingTime) {
        ViewGroup parentLayout = createOpeningTimeParentLayout();
        View dayView = createDayView(openingTime);
        View timesView = createTimesView(openingTime);
        parentLayout.addView(dayView);
        parentLayout.addView(timesView);;
        return parentLayout;

    }

    private ViewGroup createOpeningTimeParentLayout() {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(8, 8, 8, 8);
        parentLayout.setLayoutParams(lp);
        return parentLayout;
    }

    private View createDayView(OpeningTime openingTime) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(lp);
        textView.setText(openingTime.getDay().toString());
        return textView;
    }

    private View createTimesView(OpeningTime openingTime) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        );
        textView.setLayoutParams(lp);
        textView.setText(timeIntervalsToString(openingTime.getOpeningTimes()));
        textView.setGravity(Gravity.END);
        return textView;
    }

    private String timeIntervalsToString(List<TimeInterval> timeIntervals) {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = GregorianCalendar.getInstance();
        for (int i = 0; i < timeIntervals.size(); i++) {
            if (i > 0) {
                stringBuilder.append(" y ");
            }
            TimeInterval timeInterval = timeIntervals.get(i);
            calendar.setTime(timeInterval.getFromTime());
            stringBuilder.append(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
            stringBuilder.append(":");
            stringBuilder.append(String.format("%02d", calendar.get(Calendar.MINUTE)));
            stringBuilder.append(" a ");
            calendar.setTime(timeInterval.getToTime());
            stringBuilder.append(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
            stringBuilder.append(":");
            stringBuilder.append(String.format("%02d", calendar.get(Calendar.MINUTE)));
        }
        return stringBuilder.toString();
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }
}
