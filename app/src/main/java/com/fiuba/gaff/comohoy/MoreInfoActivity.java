package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.ExtrasListAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.OpeningTime;
import com.fiuba.gaff.comohoy.model.Opinion;
import com.fiuba.gaff.comohoy.model.TimeInterval;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.opinions.OnGetOpinionsCallback;
import com.fiuba.gaff.comohoy.services.opinions.OpinionsService;
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

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MoreInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 10;
    private static final String LOG_TAG = "MoreInfoActivity";

    private int mCommerceId = -1;

    private ProgressBar mOpinionsProgressBar;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mOpinionsProgressBar = findViewById(R.id.progressBar_opinions);

        obtainCommerceId(savedInstanceState);

        createOpeningTimesView();

        getOpinionsFromBackoffice();

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

    private void getOpinionsFromBackoffice() {
        showLoadingOpinions(true);
        OpinionsService opinionsService = ServiceLocator.get(OpinionsService.class);
        opinionsService.getOpinions(this, mCommerceId, new OnGetOpinionsCallback() {
            @Override
            public void onSuccess(List<Opinion> opinions) {
                createOpinionesView(opinions);
                showLoadingOpinions(false);
            }

            @Override
            public void onError(String reason) {
                showToast("No se pudieron cargar las opiniones del comercio. Intente más tarde");
                showLoadingOpinions(false);
            }
        });
    }

    private void showLoadingOpinions(boolean loading) {
        mOpinionsProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void createOpinionesView(List<Opinion> opinions) {
        ViewGroup parentLayout = findViewById(R.id.opiniones_comercio);

        for (final Opinion opinion : opinions) {
            final View opinionView = createOpinionView(opinion);
            opinionView.setClickable(true);
            opinionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(getResources().getColor(R.color.gris));
                    final Dialog _opinion = new Dialog(v.getContext());
                    _opinion.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    _opinion.setContentView(R.layout.dialog_opinion);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = _opinion.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(lp);
                    _opinion.show();
                    v.setBackgroundColor(getResources().getColor(R.color.blanco));
                    TextView nombre = window.findViewById(R.id.nombreOrigOpinion);
                    nombre.setText(opinion.getFullName());
                    TextView description = window.findViewById(R.id.descripcionOpinion);
                    description.setText(opinion.getDescription());
                    TextView rta = window.findViewById(R.id.respuesta_opinion);
                    rta.setText(opinion.getReplica());

                    LinearLayout ll = window.findViewById(R.id.puntajeOpinion);
                    int cantEstrellasAmarillas = opinion.getPuntuation();
                    int cantEstrellasNegras = 5 - cantEstrellasAmarillas;

                    for (int i = 1; i <= cantEstrellasAmarillas; i++) {
                        ImageView iv = getEstrellaAmarilla();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100 );
                        iv.setLayoutParams(params);
                        ll.addView(iv);
                    }
                    for (int i = 1; i <= cantEstrellasNegras; i++) {
                        ImageView iv = getEstrellaNegra();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100 );
                        iv.setLayoutParams(params);
                        ll.addView(iv);
                    }

                    Button cerrar = window.findViewById(R.id.appCompatButtonVolver);
                    cerrar.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View vv) {
                            _opinion.dismiss();
                        }
                    });
                }
            });
            parentLayout.addView(opinionView);
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

    private View createOpinionView(Opinion opinion) {
        ViewGroup parentLayoutMayor =  createCardView();
        parentLayoutMayor.setClickable(true);

        ViewGroup parentLayoutMenor = createOpinionParentLayout();
        ViewGroup parentLayoutMenorMenor = createOpinionParentMenorLayout();

        TextView textoNombre = getDescriptionName(opinion.getFullName());

        ViewGroup parentLayoutPuntaje = getPuntajeOpinion();

        int cantEstrellasAmarillas = opinion.getPuntuation();
        int cantEstrellasNegras = 5 - cantEstrellasAmarillas;

        for (int i = 1; i <= cantEstrellasAmarillas; i++) {
            parentLayoutPuntaje.addView(getEstrellaAmarilla());
        }
        for (int i = 1; i <= cantEstrellasNegras; i++) {
            parentLayoutPuntaje.addView(getEstrellaNegra());
        }

        parentLayoutMenorMenor.addView(textoNombre);
        parentLayoutMenorMenor.addView(parentLayoutPuntaje);

        String descripcionResumida = opinion.getDescription();
        if(opinion.getDescription().length() > 50){
            descripcionResumida = opinion.getDescription().substring(0,47) + "...";
        }
        TextView textoDesc = getDescriptionOpinion(descripcionResumida);

        parentLayoutMenor.addView(parentLayoutMenorMenor);
        parentLayoutMenor.addView(textoDesc);
        parentLayoutMayor.addView(parentLayoutMenor);
        return parentLayoutMayor;
    }

    private CardView createCardView() {
        CardView cardview = new CardView(this);
        cardview.setRadius(10.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cardview.setElevation(2);
            cardview.setCardBackgroundColor(getColor(R.color.blanco));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,1,0,1);
        cardview.setLayoutParams(params);
        return cardview;
    }

    private LinearLayout getPuntajeOpinion(){
        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(0, 2,5,0);
        ll.setGravity(Gravity.RIGHT);
        ll.setLayoutParams(lp);
        return ll;
    }

    private TextView getDescriptionName (String nombre){
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMarginStart(5);
        tv.setText(nombre);
        tv.setTextSize(18);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setLayoutParams(lp);

        return tv;
    }

    private TextView getDescriptionOpinion(String desc){
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
       lp.setMargins(5,5,5,0);
        tv.setText(desc);
        tv.setTextSize(16);
        tv.setLayoutParams(lp);
        return tv;
    }

    private ImageView getEstrellaAmarilla() {
        ImageView estrella = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                70,
                70
        );
        estrella.setLayoutParams(lp);
        estrella.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.yellowstar));
        return estrella;
    }

    private ImageView getEstrellaNegra() {
        ImageView estrella = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                70,
                70
        );
        estrella.setLayoutParams(lp);
        estrella.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.whitestar));
        return estrella;
    }

    private ViewGroup createOpinionParentMenorLayout() {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        parentLayout.setLayoutParams(lp);
        return parentLayout;
    }

    private ViewGroup createOpinionParentLayout() {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        parentLayout.setLayoutParams(lp);
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
