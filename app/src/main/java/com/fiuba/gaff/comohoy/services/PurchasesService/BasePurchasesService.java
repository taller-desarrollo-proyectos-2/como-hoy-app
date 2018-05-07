package com.fiuba.gaff.comohoy.services.PurchasesService;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fiuba.gaff.comohoy.OrderPlateActivity;
import com.fiuba.gaff.comohoy.model.purchases.Address;
import com.fiuba.gaff.comohoy.model.purchases.CreditCardDetails;
import com.fiuba.gaff.comohoy.model.purchases.PaymentDetails;
import com.fiuba.gaff.comohoy.model.purchases.PaymentMethod;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.networking.DownloadCallback;
import com.fiuba.gaff.comohoy.networking.HttpMethodType;
import com.fiuba.gaff.comohoy.networking.NetworkFragment;
import com.fiuba.gaff.comohoy.networking.NetworkObject;
import com.fiuba.gaff.comohoy.networking.NetworkResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BasePurchasesService implements PurchasesService {

    private static final String POST_ORDER_URL = "http://34.237.197.99:9000/api/v1/requests";

    private Cart mCart;
    private PaymentDetails mPaymentDetails;

    public BasePurchasesService() {
        mCart = new Cart();
        mPaymentDetails = new PaymentDetails();
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

    @Override
    public void clearPaymentDetails() {
        mPaymentDetails.reset();
    }

    @Override
    public PaymentDetails getPaymentDetails() {
        return mPaymentDetails;
    }

    @Override
    public void setPaymentDetails(PaymentDetails paymentDetails) {
        mPaymentDetails = paymentDetails;
    }

    @Override
    public void submitPurchase(Activity activity, final OnSubmitPurchaseCallback callback) {
        NetworkObject networkObject = createSubmitOrderNetworkObject();
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), networkObject);
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {
            @Override
            public void onResponseReceived(@NonNull NetworkResult result) {
                if (result.mException == null) {
                    callback.onSuccess();
                } else {
                    callback.onError("Error al realizar la orden, por favor intente nuevamente");
                }
            }

            @Override
            public NetworkInfo getActiveNetworkInfo(Context context) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo;
            }

            @Override
            public void onProgressUpdate(int progressCode, int percentComplete) {
            }

            @Override
            public void onFinishDownloading() {
            }
        });
        clearCart();
        clearPaymentDetails();
    }

    private NetworkObject createSubmitOrderNetworkObject() {
        String requestBody = createSubmitPurchaseJson().toString();
        NetworkObject networkObject = new NetworkObject(POST_ORDER_URL, HttpMethodType.POST, requestBody);
        return networkObject;
    }

    private JSONObject createSubmitPurchaseJson() {
        JSONObject purchaseJson = new JSONObject();
        try {
            purchaseJson.put("singleRequests", createOrderJson());
            purchaseJson.put("destination", createDestinationJson());
            purchaseJson.put("paymentType", createPaymentDetailsJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return purchaseJson;
    }

    private JSONArray createOrderJson() {
        JSONArray ordersJson = new JSONArray();
        List<PlateOrder> mPlateOrders = mCart.getOrders();
        for (PlateOrder plateOrder : mPlateOrders) {
            try {
                JSONObject orderJson = new JSONObject();
                orderJson.put("plate", plateOrder.getPlateId());
                orderJson.put("optionals", createExtrasJson(plateOrder));
                orderJson.put("comment", plateOrder.getClarifications());
                orderJson.put("quantity", plateOrder.getQuantity());

                ordersJson.put(orderJson);
            } catch (JSONException e) {
                Log.w("PurchaseService", "Plate with id " + plateOrder.getPlateId() + " could not be converted to json.");
                e.printStackTrace();
            }
        }
        return ordersJson;
    }

    private JSONArray createExtrasJson(PlateOrder plateOrder) {
        JSONArray extrasJson = new JSONArray();
        List<Long> extrasId = plateOrder.getExtrasId();
        for (Long extraId : extrasId) {
            try {
                JSONObject extraJson = new JSONObject();
                extraJson.put("id", extraId);
                extrasJson.put(extraJson);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return extrasJson;
    }

    private JSONObject createDestinationJson() throws JSONException {
        JSONObject destinationJson = new JSONObject();
        Address destinationAddress = mPaymentDetails.getShippingAddress();
        destinationJson.put("street", destinationAddress.getStreetName());
        destinationJson.put("number", destinationAddress.getStreetNumber());
        destinationJson.put("additionalInformation", destinationAddress.getAdditionalInformation());
        return destinationJson;
    }

    private JSONObject createPaymentDetailsJson () throws JSONException {
        JSONObject paymentDetailsJson = new JSONObject();
        PaymentMethod paymentMethod = mPaymentDetails.getPaymentMethod();
        paymentDetailsJson.put("PAYMENT_TYPE", getPaymentMethodString(paymentMethod));
        switch (paymentMethod) {
            case Cash:
                paymentDetailsJson.put("payWith", mCart.getTotalPrice());
                break;
            case CreditCard:
                CreditCardDetails cardDetails = mPaymentDetails.getCardDetails();
                paymentDetailsJson.put("number", cardDetails.getCardNumber());
                paymentDetailsJson.put("fullName", cardDetails.getOwnerName());
                paymentDetailsJson.put("expirationDate", cardDetails.getExpireDate());
                paymentDetailsJson.put("code", cardDetails.getSecurityNumber());
        }
        return paymentDetailsJson;
    }

    private String getPaymentMethodString(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case Cash: return "CASH";
            case CreditCard: return "CREDIT_CARD";
            default: return "CASH";
        }
    }
}
