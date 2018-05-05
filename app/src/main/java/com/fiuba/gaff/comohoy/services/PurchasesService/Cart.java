package com.fiuba.gaff.comohoy.services.PurchasesService;

import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cart {
    private int mCommerceId;
    private List<PlateOrder> mOrders;

    public Cart() {
        mOrders = new ArrayList<>();
    }

    public int getCommerceId() {
        return mCommerceId;
    }

    public void setCommerceId(int commerceId) {
        mCommerceId = commerceId;
    }

    public void addPlateOrder(PlateOrder order) {
        mOrders.add(order);
    }
    public List<PlateOrder> getOrders() {
        return mOrders;
    }

    public void setOrders(List<PlateOrder> orders) {
        mOrders = orders;
    }

    public void clear() {
        mOrders.clear();
    }

    public boolean isEmpty() {
        return mOrders.isEmpty();
    }

    public int size() {
        return mOrders.size();
    }

    public double getTotalPrice() {
        double total = 0;
        for (PlateOrder order : mOrders) {
            total += order.getOrderPrice();
        }
        return total;
    }

    public void removePlateOrder(Long orderId) {
        Iterator<PlateOrder> itr = mOrders.iterator();
        while (itr.hasNext())
        {
            if (orderId.equals(itr.next().getOrderId())) {
                itr.remove();
                break;
            }
        }
    }

    public void modifyPlateOrder(Long orderId, PlateOrder plateOrder) {
        Iterator<PlateOrder> itr = mOrders.iterator();
        while (itr.hasNext())
        {
            if (orderId.equals(itr.next().getOrderId())) {
                itr.remove();
                mOrders.add(plateOrder);
                break;
            }
        }
    }
}
