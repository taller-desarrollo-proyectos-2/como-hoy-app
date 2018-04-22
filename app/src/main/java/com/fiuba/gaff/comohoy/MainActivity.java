package com.fiuba.gaff.comohoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Plate;

public class MainActivity extends AppCompatActivity implements CommercesListFragment.CommerceListListener{

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onCommerceClicked(Commerce commerce, View commerceTitleTextView) {
        Intent intent = new Intent();
        intent.setClass(this, CommerceDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_data_commerce_id), commerce.getId());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, commerceTitleTextView, getString(R.string.transition_commerce_title));

        startActivity(intent, options.toBundle());
    }

}
