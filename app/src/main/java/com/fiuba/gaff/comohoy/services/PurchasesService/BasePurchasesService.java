package com.fiuba.gaff.comohoy.services.PurchasesService;

import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;

import java.util.HashMap;
import java.util.Map;

public class BasePurchasesService implements PurchasesService {

    private Map<Long, PlateOrder> mCart;

    public BasePurchasesService() {
        mCart = new HashMap<>();
    }

    @Override
    public void addPlateToCart(PlateOrder plateOrder) {
        mCart.put(plateOrder.getOrderId(), plateOrder);
    }
}
