package com.fiuba.gaff.comohoy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

public class CommercesMoreInfoActivity extends AppCompatActivity {

    private int mCommerceId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_restourant);

        obtainCommerceId(savedInstanceState);

        Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        // TODO llenar valores de la vista con los valores del comercio
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(getString(R.string.intent_data_commerce_id), mCommerceId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCommerceId = savedInstanceState.getInt(getString(R.string.intent_data_commerce_id));
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

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }
}
