package com.fiuba.gaff.comohoy.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.fiuba.gaff.comohoy.CommerceDetailsActivity;
import com.fiuba.gaff.comohoy.Manifest;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.adapters.CategoriesAdapter;
import com.fiuba.gaff.comohoy.adapters.CommerceInfoWindowAdapter;
import com.fiuba.gaff.comohoy.adapters.CommerceListAdapter;
import com.fiuba.gaff.comohoy.filters.CategoryFilter;
import com.fiuba.gaff.comohoy.filters.DistanceFilter;
import com.fiuba.gaff.comohoy.filters.PriceFilter;
import com.fiuba.gaff.comohoy.filters.RatingFilter;
import com.fiuba.gaff.comohoy.filters.SearchFilter;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.CategoryUsageData;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.commerces.SortCriteria;
import com.fiuba.gaff.comohoy.services.commerces.UpdateCommercesCallback;
import com.fiuba.gaff.comohoy.services.location.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class CommercesListFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 10;

    private boolean mFilterCommercesByDistance = false;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mFiltersButton;
    private View mCategoriesButton;
    private FloatingActionButton mChangeViewButton;
    private SearchView mSearchView;

    private Category electedCategory;

    private GridView mCategoriesGridView;
    private FrameLayout mCategorie;
    private TextView mCategorieName;

    private SortCriteria mSortCriteria = SortCriteria.Closeness;
    private Dialog mCategoriesDialog;
    private Dialog mFiltersDialog;

    private CommerceListListener mCommerceListListener;

    private boolean mOnMapView = false;

    private boolean p1 = false;
    private boolean p2 = false;
    private boolean p3 = false;
    private float max_distancia;
    private float min_distancia;
    private float max_puntaje;
    private float min_puntaje;

    private View mMapView;
    private GoogleMap mMap;

    private HashMap<Marker, Commerce> mCommercesFromMarkerMap = new HashMap();

    private GoogleMap.OnMyLocationClickListener mOnMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull android.location.Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        //uiSettings.setZoomControlsEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
        enableMyLocationIfPermitted();

        mCommercesFromMarkerMap.clear();
        List<Commerce> listaComercios = getCommercesService().getCommercesSortedBy(getActivity(), mSortCriteria);
        for (Commerce commerce : listaComercios) {
            double latitud = commerce.getLocation().getLatitud();
            double longitud = commerce.getLocation().getLongitud();
            LatLng location = new LatLng(latitud, longitud);
            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(commerce.getShowableName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mCommercesFromMarkerMap.put(marker, commerce);
        }
        LocationService locationService = ServiceLocator.get(LocationService.class);
        Location mCurrentLocation = locationService.getLocation(getContext());
        LatLng location = new LatLng(mCurrentLocation.getLatitud(), mCurrentLocation.getLongitud());
        //mMap.addMarker(new MarkerOptions().position(location).title("Aquí estoy").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        float zoomLevel = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
        if (mMapView != null) {
            mMapView.setVisibility(View.GONE);
        }
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new CommerceInfoWindowAdapter(LayoutInflater.from(getActivity()), mCommercesFromMarkerMap));
        // mMap.setOnMyLocationClickListener(mOnMyLocationClickListener);
    }

    private void enableMyLocationIfPermitted() {
        final int LOCATION_PERMISSION_REQUEST_CODE = 1;
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Commerce commerce = mCommercesFromMarkerMap.get(marker);
        if (commerce == null) return;

        Intent intent = new Intent();
        intent.setClass(getActivity(), CommerceDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_data_commerce_id), commerce.getId());
        intent.putExtra(getString(R.string.intent_data_commerce_longitud_id), commerce.getLocation().getLongitud());
        intent.putExtra(getString(R.string.intent_data_commerce_latitud_id), commerce.getLocation().getLatitud());
        intent.putExtra(getString(R.string.intent_data_fromFavourites), false);
        startActivity(intent);
    }

    public interface CommerceListListener {
        void onCommerceClicked(Commerce commerce, View commerceTitleTextView);
    }

    public CommercesListFragment() {
    }

    public static CommercesListFragment newInstance(int columnCount) {
        CommercesListFragment fragment = new CommercesListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Location currentLocation = getLocationService().getLocation(getActivity());
        if (mFilterCommercesByDistance) {
            getCommercesService().updateCommercesWithLocation(getActivity(), createOnUpdatedCommercesCallback(), currentLocation);
        } else {
            getCommercesService().updateCommercesData(getActivity(), createOnUpdatedCommercesCallback());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview_commerces_list);
        mProgressBar = view.findViewById(R.id.progress_bar_commerces_list);
        mFiltersButton = view.findViewById(R.id.action_button_filter);
        mChangeViewButton = view.findViewById(R.id.action_button_map_view);
        mCategoriesButton = view.findViewById(R.id.action_button_categories);
        mSearchView = view.findViewById(R.id.searchView);
        mMapView = view.findViewById(R.id.commerce_map);
        mCategorieName = view.findViewById(R.id.id_nombre_de_categoria);
        mCategorie = view.findViewById(R.id.categoria_fl);
        electedCategory = null;

        mOnMapView = false;

        if (getCommercesService().isDownloading()) {
            showProgress(true);
        } else {
            showProgress(false);
        }

        createFiltersDialog();
        setUpSearchView();
        setUpFilterButton(view);
        setUpCategoriButton(view);
        setUpChangeViewButton(view);
        setUpMapView();
        fixedFloatingButtonsPosition();

        updateMainView(mOnMapView);

        return view;
    }

    private void setUpMapView(){
        //map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        Activity mContext = getActivity();
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(mContext);
        if (resultCode == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.commerce_map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = googleAPI.getErrorDialog(mContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            dialog.show();
        }
    }

    private void updateMainView(boolean onMapView) {
        if (onMapView) {
            mSearchView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mMapView.setVisibility(View.VISIBLE);
        } else {
            mMapView.setVisibility(View.GONE);
            mSearchView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        updateButtonsView(mOnMapView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommerceListListener) {
            mCommerceListListener = (CommerceListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CommerceListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommerceListListener = null;
    }

    private void setUpSearchView() {
        mSearchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                filterCommercesByText(newText, false);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCommercesByText(query, true);
                return true;
            }
        });
    }

    private void filterCommercesByText(String searchText, boolean notifyNoneCommerces) {
        List<Commerce> commerces = getCommercesService().getCommerces();
        if (commerces != null) {
            SearchFilter saerchFilter = new SearchFilter(searchText);
            List<Commerce> filteredCommerces = saerchFilter.apply(commerces);
            loadCommerces(filteredCommerces, notifyNoneCommerces);
        }
    }

    private void loadCommerces(List<Commerce> commerces, boolean notifyNoneCommerces) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new CommerceListAdapter(commerces, mCommerceListListener));
        if (notifyNoneCommerces && commerces.size() < 1) {
            Toast.makeText(getContext(), "No contamos con comercios en este momento", Toast.LENGTH_LONG).show();
        }
    }

    private CommercesService getCommercesService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private LocationService getLocationService() {
        return ServiceLocator.get(LocationService.class);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        mFiltersButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mCategoriesButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mChangeViewButton.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private UpdateCommercesCallback createOnUpdatedCommercesCallback() {
        return new UpdateCommercesCallback() {
            @Override
            public void onCommercesUpdated() {
                loadCommerces(getCommercesService().getCommercesSortedBy(getActivity(), mSortCriteria), true);
                showProgress(false);
            }

            @Override
            public void onError(String reason) {
                Toast.makeText(getContext(), reason, Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        };
    }

    private void setUpFilterButton(View fragmentView) {
        FloatingActionButton filterButton =  fragmentView.findViewById(R.id.action_button_filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFiltersDialog.show();
            }
        });
    }

    private void setUpCategoriButton(View fragmentView){
        FloatingActionButton filterButton =  fragmentView.findViewById(R.id.action_button_categories);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCategoriesDialog == null) {
                    createCategoriesDialog();
                }
                mCategoriesDialog.show();
            }
        });
    }

    private void setUpChangeViewButton(View fragmentView){
        mChangeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int animationTimeInMill = 400;
                float target = mOnMapView ? 1f : -1f;
                ObjectAnimator.ofFloat(mChangeViewButton, "scaleX", target).setDuration(animationTimeInMill).start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnMapView) {
                            mChangeViewButton.setImageDrawable(getResources().getDrawable(R.drawable.map_icon2));
                            mOnMapView = false;
                        } else {
                            mChangeViewButton.setImageDrawable(getResources().getDrawable(R.drawable.list_icon));
                            mOnMapView = true;
                        }
                        updateMainView(mOnMapView);
                    }
                }, animationTimeInMill / 2);
            }
        });
    }

    private void updateButtonsView(boolean onMapView) {
        if (onMapView) {
            mCategoriesButton.setAlpha(0.5f);
            mCategoriesButton.setClickable(false);
            mFiltersButton.setAlpha(0.5f);
            mFiltersButton.setClickable(false);
        } else {
            mCategoriesButton.setAlpha(1.0f);
            mCategoriesButton.setClickable(true);
            mFiltersButton.setAlpha(1.0f);
            mFiltersButton.setClickable(true);
        }
    }

    private void createCategoriesDialog() {
        mCategoriesDialog = new Dialog(getContext());
        mCategoriesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCategoriesDialog.setContentView(R.layout.dialog_categories);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mCategoriesDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        List<CategoryUsageData> categoriesUsageData = getCommercesService().getUsedCategoriesUsageData();
        GridView gridview = mCategoriesDialog.findViewById(R.id.gridCategories);
        if (categoriesUsageData != null && gridview != null){
            gridview.setAdapter(new CategoriesAdapter(getActivity(), categoriesUsageData, new CategoriesAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(String categoryName) {
                    CategoryFilter categoryFilter = new CategoryFilter(categoryName);
                    getCommercesService().clearCategoryFilters();
                    getCommercesService().addCategoryFilter(categoryFilter);
                    List<Commerce> filteredCommerces = getCommercesService().getCommerces();
                    loadCommerces(filteredCommerces, true);
                    mCategoriesDialog.dismiss();
                }
            }));
        }

        Button removeFiltesButton = window.findViewById(R.id.button_quitar_filtros);
        removeFiltesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CommercesService commercesService = getCommercesService();
                commercesService.clearCategoryFilters();
                List<Commerce> commerces = commercesService.getCommercesSortedBy(getActivity(), mSortCriteria);
                loadCommerces(commerces, false);
                mCategoriesDialog.dismiss();
            }
        });

        /*final ImageView quitar = window.findViewById(R.id.quitardialogo);
        quitar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                electedCategory = null;
                quitar.setVisibility(View.GONE);
                mCategoriesDialog.dismiss();
            }
        });*/

        //window.setContentView(mSVCategories);
    }

    private void createFiltersDialog() {
        mFiltersDialog = new Dialog(getContext());
        mFiltersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mFiltersDialog.setContentView(R.layout.dialog_filters2);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mFiltersDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Spinner spinner = mFiltersDialog.findViewById(R.id.spinner_sort_criteria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_criteria_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selectedItem = adapterView.getItemAtPosition(pos).toString();
                switch (selectedItem) {
                    case "Cercanía":
                        mSortCriteria = SortCriteria.Closeness;
                        break;
                    case "Puntaje":
                        mSortCriteria = SortCriteria.Rating;
                        break;
                    case "Tiempo de Envío":
                        mSortCriteria = SortCriteria.ShipTime;
                        break;
                    case "Precio":
                        mSortCriteria = SortCriteria.Price;
                        break;
                    default: mSortCriteria = SortCriteria.Closeness;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) mFiltersDialog.findViewById(R.id.rangeSeekbar1);
        rangeSeekbar.setMinValue(1);
        rangeSeekbar.setMaxValue(5);
        max_puntaje = 5;
        min_puntaje = 1;
        rangeSeekbar.setDataType(CrystalRangeSeekbar.DataType.FLOAT);
        // get min and max text view
        final TextView tvMin = (TextView) mFiltersDialog.findViewById(R.id.min_value_1);
        final TextView tvMax = (TextView) mFiltersDialog.findViewById(R.id.max_value_1);
        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.format("%.1f",minValue.floatValue()));
                tvMax.setText(String.format("%.1f",maxValue.floatValue()));
                max_puntaje = maxValue.floatValue();
                min_puntaje = minValue.floatValue();
            }
        });
        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        final CrystalRangeSeekbar rangeSeekbar2 = (CrystalRangeSeekbar) mFiltersDialog.findViewById(R.id.rangeSeekbar2);
        rangeSeekbar2.setMinValue(0);
        rangeSeekbar2.setMaxValue(10);
        max_distancia = 0;
        min_distancia = 10;
        rangeSeekbar2.setDataType(CrystalRangeSeekbar.DataType.FLOAT);
        // get min and max text view
        final TextView tvMin2 = (TextView) mFiltersDialog.findViewById(R.id.min_value_2);
        final TextView tvMax2 = (TextView) mFiltersDialog.findViewById(R.id.max_value_2);
        // set listener
        rangeSeekbar2.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin2.setText(String.format("%.1f km",minValue.floatValue()));
                tvMax2.setText(String.format("%.1f km",maxValue.floatValue()));
                max_distancia = maxValue.floatValue();
                min_distancia = minValue.floatValue();
            }
        });
        // set final value listener
        rangeSeekbar2.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        final ImageView id1 = mFiltersDialog.findViewById(R.id.d1);
        final ImageView id2 = mFiltersDialog.findViewById(R.id.d2);
        final ImageView id3 = mFiltersDialog.findViewById(R.id.d3);
        final ImageView id4 = mFiltersDialog.findViewById(R.id.d4);
        final ImageView id5 = mFiltersDialog.findViewById(R.id.d5);
        final ImageView id6 = mFiltersDialog.findViewById(R.id.d6);

        id1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
        id2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
        id3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
        id4.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
        id5.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
        id6.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));

        LinearLayout precio1 = mFiltersDialog.findViewById(R.id.precio_elegido_1);
        precio1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p1){
                    id1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_green_t));
                    id2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id4.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id5.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id6.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    p1 = true;
                    p2 = false;
                    p3 = false;
                }
                else{
                    id1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    p1 = false;
                }
            }
        });

        LinearLayout precio2 = mFiltersDialog.findViewById(R.id.precio_elegido_2);
        precio2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p2){
                    id1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_green_t));
                    id3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_green_t));
                    id4.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id5.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id6.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    p1 = false;
                    p2 = true;
                    p3 = false;
                }
                else{
                    id2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    p2 = false;
                }
            }
        });

        LinearLayout precio3 = mFiltersDialog.findViewById(R.id.precio_elegido_3);
        precio3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p3){
                    id1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id4.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_green_t));
                    id5.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_green_t));
                    id6.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_green_t));
                    p1 = false;
                    p2 = false;
                    p3 = true;
                }
                else{
                    id4.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id5.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    id6.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dollar_empty));
                    p3 = false;
                }
            }
        });

        Button acceptButton = mFiltersDialog.findViewById(R.id.button_accept);
        Button cancelButton = mFiltersDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistanceFilter dFilter = new DistanceFilter(0,99999999,getContext());
                float minPrice = 0;
                float maxPrice = PriceFilter.INFINITE;
                if (p1){ minPrice = 0;maxPrice = 250;}
                if (p2){ minPrice = 250;maxPrice = 500;}
                if (p3){ minPrice = 500;maxPrice = PriceFilter.INFINITE;}
                PriceFilter pFilter = new PriceFilter(minPrice, maxPrice);
                RatingFilter rFilter = new RatingFilter(min_puntaje,max_puntaje);

                getCommercesService().clearFilters();
                getCommercesService().addFilter(dFilter);
                getCommercesService().addFilter(rFilter);
                getCommercesService().addFilter(pFilter);

                List<Commerce> sortedCommerces = getCommercesService().getCommercesSortedBy(getActivity(), mSortCriteria);
                loadCommerces(sortedCommerces, false);
                mFiltersDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFiltersDialog.dismiss();
            }
        });
    }

    private void fixedFloatingButtonsPosition() {
        final TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        final AppBarLayout appBarLayout = getActivity().findViewById(R.id.main_app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxAbsOffset = appBarLayout.getMeasuredHeight() - tabLayout.getMeasuredHeight();
                mFiltersButton.setTranslationY(-maxAbsOffset - verticalOffset);
                mCategoriesButton.setTranslationY(-maxAbsOffset - verticalOffset);
                mChangeViewButton.setTranslationY(-maxAbsOffset - verticalOffset);
                mMapView.setTranslationY(-maxAbsOffset - verticalOffset);
            }

        });
    }
}

