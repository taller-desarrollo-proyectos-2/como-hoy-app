package com.fiuba.gaff.comohoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fiuba.gaff.comohoy.fragments.CommercesListFragment;
import com.fiuba.gaff.comohoy.fragments.FavouritesFragment;
import com.fiuba.gaff.comohoy.fragments.MyOrdersFragment;
import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CommercesListFragment.CommerceListListener {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mViewPager = findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.restaurant_3);
        mTabLayout.getTabAt(1).setIcon(R.drawable.favoritos);
        mTabLayout.getTabAt(2).setIcon(R.drawable.orders);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CommercesListFragment(), "Comercios");
        adapter.addFragment(new FavouritesFragment(), "Favoritos");
        adapter.addFragment(new MyOrdersFragment(), "Mis órdenes");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCommerceClicked(Commerce commerce, View commerceTitleTextView) {
        Intent intent = new Intent();
        intent.setClass(this, CommerceDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_data_commerce_id), commerce.getId());
        intent.putExtra(getString(R.string.intent_data_commerce_longitud_id), commerce.getLocation().getLongitud());
        intent.putExtra(getString(R.string.intent_data_commerce_latitud_id), commerce.getLocation().getLatitud());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, commerceTitleTextView, getString(R.string.transition_commerce_title));

        startActivity(intent, options.toBundle());
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
