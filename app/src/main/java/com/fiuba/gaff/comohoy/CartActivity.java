package com.fiuba.gaff.comohoy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.adapters.PlatesOrderAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.services.PurchasesService.Cart;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private Commerce mCommerce;
    private Cart mCart;

    public interface PlatesOrderListListener {
        void onPlateOrderClicked(PlateOrder plateOrder);
        void onPlateOrderRemoved();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_cart);

        mCart = getPurchaseService().getCart();
        mCommerce = getCommerceService().getCommerce(mCart.size());

        setCommerceTitleTextView();
        setCartCost();
        setupPlateOrdersView();
        setupAddMorePlatesButton();
        setupContinuePurchaseButton();
    }

    private void setCommerceTitleTextView() {
        TextView textView = findViewById(R.id.commerce_title);
        textView.setText(mCommerce.getShowableName());
    }

    private void setCartCost() {
        TextView totalCostView = findViewById(R.id.textView_costo_total);
        totalCostView.setText(String.format(Locale.ENGLISH, "Total: $%.2f", mCart.getTotalPrice()));
    }

    private void setupAddMorePlatesButton() {
        Button addMorePlatedButton = findViewById(R.id.button_add_more_plates);
        addMorePlatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToCommerceDetailsActivity();
            }
        });
    }

    private void setupContinuePurchaseButton() {
        Button continuePurchaseButton = findViewById(R.id.button_continue_purchase);
        continuePurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Continue with purchase
            }
        });
    }

    private void setupPlateOrdersView() {
        RecyclerView plateOrdersRecyclerView = findViewById(R.id.plates_list);
        plateOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<PlateOrder> plateOrders = mCart.getOrders();
        plateOrdersRecyclerView.setAdapter(new PlatesOrderAdapter(plateOrders, new PlatesOrderListListener() {
            @Override
            public void onPlateOrderClicked(PlateOrder plateOrder) {
                // open modify plate order activity
            }

            @Override
            public void onPlateOrderRemoved() {
                if (getPurchaseService().isCartEmpty()) {
                    goBackToCommerceDetailsActivity();
                }
            }
        }));
    }

    private void goBackToCommerceDetailsActivity() {
        finish();
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }
}
