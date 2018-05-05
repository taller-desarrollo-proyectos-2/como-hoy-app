package com.fiuba.gaff.comohoy.services.PurchasesService;

import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cart {
    private int mCommerceId;
    private List<PlateOrder> mOrders;

    private int mLastId;

    public Cart() {
        mOrders = new ArrayList<>();
        mLastId = 0;
    }

    public int getCommerceId() {
        return mCommerceId;
    }

    public void setCommerceId(int commerceId) {
        mCommerceId = commerceId;
    }

    public void addPlateOrder(PlateOrder order) {
        order.setOrderId(Long.valueOf(mLastId));
        mOrders.add(order);
        mLastId++;
    }
    public List<PlateOrder> getOrders() {
        return mOrders;
    }

    public void setOrders(List<PlateOrder> orders) {
        mOrders = orders;
    }

    public void clear() {
        mLastId = 0;
        mOrders.clear();
    }

    public boolean isEmpty() {
        return mOrders.isEmpty();
    }

    public int size() {
        return mOrders.size();
    }

    public PlateOrder getPlateOrder(Long id) {
        for (int i = 0; i < mOrders.size(); i++) {
            PlateOrder plateOrder = mOrders.get(i);
            if (plateOrder.getOrderId().equals(id)) {
                return plateOrder;
            }
        }
        return null;
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
                plateOrder.setOrderId(orderId);
                mOrders.add(plateOrder);
                break;
            }
        }
    }
}
