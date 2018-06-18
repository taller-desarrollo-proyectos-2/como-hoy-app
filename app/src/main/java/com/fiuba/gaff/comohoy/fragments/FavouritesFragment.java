package com.fiuba.gaff.comohoy.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.CommerceDetailsActivity;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.adapters.CommerceListAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.List;


public class FavouritesFragment extends Fragment {

    private static final int FRAGMENT_TAB_INDEX = 1;

    private RecyclerView mRecyclerView;
    private ProgressBar mFavouritesProgressBar;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setUpOnPageChangeListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview_favourites_commerces_list);
        mFavouritesProgressBar = view.findViewById(R.id.progressBar_favourites);

        return view;
    }

    private void setUpOnPageChangeListener() {
        ViewPager viewPager = getActivity().findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == FRAGMENT_TAB_INDEX) {
                    showFavouritesCommerces(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showFavouritesCommerces(true);
    }

    private void showFavouritesCommerces(boolean fromResume) {
        showLoadingFavourites(true);
        List<Commerce> favouritesCommerces = getCommercesService().getFavouritesCommerces();
        loadCommerces(favouritesCommerces, true, fromResume);
    }

    private void loadCommerces(List<Commerce> commerces, boolean notifyNoneCommerces, boolean fromResume) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new CommerceListAdapter(commerces, new CommercesListFragment.CommerceListListener() {
            @Override
            public void onCommerceClicked(Commerce commerce, View commerceTitleTextView) {
                openCommerceDetailsScreen(commerce, commerceTitleTextView);
                showLoadingFavourites(true);
            }
        }));
        if (!fromResume && notifyNoneCommerces && commerces.size() < 1) {
            Toast.makeText(getContext(), "No ha seleccionado ningún comercio como favorito.", Toast.LENGTH_SHORT).show();
        }
        showLoadingFavourites(false);
    }

    private void openCommerceDetailsScreen(Commerce commerce, View commerceTitleTextView) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CommerceDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_data_commerce_id), commerce.getId());
        intent.putExtra(getString(R.string.intent_data_commerce_longitud_id), commerce.getLocation().getLongitud());
        intent.putExtra(getString(R.string.intent_data_commerce_latitud_id), commerce.getLocation().getLatitud());
        startActivity(intent);
    }

    private void showLoadingFavourites(boolean loading) {
        mFavouritesProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
    }

    private CommercesService getCommercesService() {
        return ServiceLocator.get(CommercesService.class);
    }

}
