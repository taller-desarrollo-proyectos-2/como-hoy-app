package com.fiuba.gaff.comohoy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.adapters.SingleRequestAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.purchases.backend.Request;
import com.fiuba.gaff.comohoy.model.purchases.backend.SingleRequest;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.List;

public class SeeOrderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Request mRequest;
    private Long mRequestId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_see_request);

        mRecyclerView = findViewById(R.id.single_request_list);

        obtainRequestId(savedInstanceState);
        mRequest = getPurchaseService().getRequestWithId(mRequestId);

        setUpCommerceTitle();
        setUpOrderPrice();

        loadSingleRequestsList(mRequest.getSingleRequests());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(getString(R.string.intent_data_request_id), mRequestId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRequestId = savedInstanceState.getLong(getString(R.string.intent_data_request_id), -1);
    }

    private void setUpCommerceTitle() {
        TextView commerceTitle = findViewById(R.id.commerce_title);
        Commerce commerce = getCommerceService().getCommerce(mRequest.getCommerceId());
        if (commerce != null) {
            String commerceName = getCommerceService().getCommerce(mRequest.getCommerceId()).getShowableName();
            commerceTitle.setText(commerceName);
        } else {
            commerceTitle.setVisibility(View.GONE);
        }
    }

    private void setUpOrderPrice() {
        TextView orderPriceText = findViewById(R.id.textView_costo_total);
        orderPriceText.setText(String.format("Total: $%.2f", mRequest.getPrice()));
    }

    private void loadSingleRequestsList(List<SingleRequest> orders) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SingleRequestAdapter(orders));
    }

    private void obtainRequestId(Bundle savedInstanceState) {
        if (mRequestId.equals(-1L)) {
            Bundle extras = getIntent().getExtras();
            mRequestId = extras.getLong(getString(R.string.intent_data_request_id), -1L);
        }
        if ((mRequestId.equals(-1L)) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_commerce_id)))) {
            mRequestId = savedInstanceState.getLong(getString(R.string.intent_data_request_id));
        }
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }
}
