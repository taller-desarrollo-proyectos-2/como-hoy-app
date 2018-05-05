package com.fiuba.gaff.comohoy.services.PurchasesService;

import com.fiuba.gaff.comohoy.OrderPlateActivity;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

public class BasePurchasesService implements PurchasesService {

    private Cart mCart;

    public BasePurchasesService() {
        mCart = new Cart();
    }

    @Override
    public Cart getCart() {
        return mCart;
    }

    @Override
    public void assignCommerce(int commerceId) {
        mCart.setCommerceId(commerceId);
    }

    @Override
    public void addPlateOrderToCart(PlateOrder plateOrder) {
        mCart.addPlateOrder(plateOrder);
    }

    @Override
    public void modifyPlateOrder(Long orderId, PlateOrder plateOrder) {
        mCart.modifyPlateOrder(orderId, plateOrder);
    }

    @Override
    public void removePlateOrder(Long orderId) {
        mCart.removePlateOrder(orderId);
    }

    @Override
    public void clearCart() {
        mCart.clear();
    }

    @Override
    public boolean isCartEmpty() {
        return mCart.isEmpty();
    }

}
