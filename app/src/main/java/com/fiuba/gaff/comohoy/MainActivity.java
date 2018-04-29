package com.fiuba.gaff.comohoy;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.MockCommerceSuggestion;

public class MainActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        //android.support.v7.widget.SearchView.OnQueryTextListener,
        //SearchView.OnQueryTextListener,
        CommercesListFragment.CommerceListListener{

    private MockCommerceSuggestion suggestion;
    private TextView mTextMessage;

    public interface SearchListener {
        void onSearchCommerce(String commerce);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //suggestion = new MockCommerceSuggestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        MenuItem searchItem = menu.findItem(R.id.search_comercio);
/*        SearchView searchView = (SearchView) searchItem.getActionView();
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            //searchView.setOnQueryTextListener(this);
            SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo( new ComponentName(this, MainActivity.class)));
            searchView.setIconifiedByDefault(false);
        }*/
        return true;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
