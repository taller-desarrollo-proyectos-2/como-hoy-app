package com.fiuba.gaff.comohoy.services.PurchasesService;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.model.TimeInterval;
import com.fiuba.gaff.comohoy.model.purchases.Address;
import com.fiuba.gaff.comohoy.model.purchases.CreditCardDetails;
import com.fiuba.gaff.comohoy.model.purchases.PaymentDetails;
import com.fiuba.gaff.comohoy.model.purchases.PaymentMethod;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.model.purchases.backend.Request;
import com.fiuba.gaff.comohoy.model.purchases.RequestStatus;
import com.fiuba.gaff.comohoy.model.purchases.backend.SingleRequest;
import com.fiuba.gaff.comohoy.networking.DownloadCallback;
import com.fiuba.gaff.comohoy.networking.HttpMethodType;
import com.fiuba.gaff.comohoy.networking.NetworkFragment;
import com.fiuba.gaff.comohoy.networking.NetworkObject;
import com.fiuba.gaff.comohoy.networking.NetworkResult;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.facebook.FacebookService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BasePurchasesService implements PurchasesService {

    private static final String POST_ORDER_URL = "http://34.237.197.99:9000/api/v1/requests";
    private static final String GET_ORDERS_URL = "http://34.237.197.99:9000/api/v1/requests";

    private Cart mCart;
    private PaymentDetails mPaymentDetails;
    private List<Request> mUserOrders;

    public BasePurchasesService() {
        mUserOrders = new ArrayList<>();
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

    @Override
    public void updateOrder(Long id, RequestStatus status, Activity activity, final OnRequestUpdatedCallback callback) {
        NetworkObject updateRequestNetworkObject = createUpdateRequestNetworkObject(id, status);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), updateRequestNetworkObject);
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {
            @Override
            public void onResponseReceived(@NonNull NetworkResult result) {
                if (result.mException == null) {
                    callback.onSuccess();
                } else {
                    callback.onError("No se pudo actualizar su orden");
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
    }

    @Override
    public List<Request> getOrdersCached() {
        return mUserOrders;
    }

    @Override
    public void getOrdersFromServer(Activity activity, final OnGetOrdersCallback callback) {
        NetworkObject getRequestsNetworkObject = createGetRequestsNetworkObject();
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), getRequestsNetworkObject);
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {
            @Override
            public void onResponseReceived(@NonNull NetworkResult result) {
                if (result.mException == null) {
                    try {
                        parseOrdersFromResponse(result.mResultValue);
                        callback.onSuccess(mUserOrders);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("No se pudiieron obetener sus órdenes. Revise su conección");
                    }
                } else {
                    callback.onError("No se pudiieron obetener sus órdenes. Revise su conección");
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
    }

    private NetworkObject createSubmitOrderNetworkObject() {
        String requestBody = createSubmitPurchaseJson().toString();
        NetworkObject networkObject = new NetworkObject(POST_ORDER_URL, HttpMethodType.POST, requestBody);
        networkObject.setAuthToken(ServiceLocator.get(FacebookService.class).getAuthToken());
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
                JSONObject plateIdJson = new JSONObject();
                plateIdJson.put("id", plateOrder.getPlateId());
                orderJson.put("plate", plateIdJson);
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
                paymentDetailsJson.put("code", Integer.valueOf(cardDetails.getSecurityNumber()));
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

    private NetworkObject createUpdateRequestNetworkObject(Long updateRequestId, RequestStatus statusToUpdate) {
        String putBody = createUpdateRequestBody(statusToUpdate);
        String uri = String.format("%s/%d", POST_ORDER_URL, updateRequestId);
        NetworkObject networkObject = new NetworkObject(uri, HttpMethodType.PUT, putBody);
        networkObject.setAuthToken(ServiceLocator.get(FacebookService.class).getAuthToken());
        return networkObject;
    }

    private String createUpdateRequestBody(RequestStatus statusToUpdate) {
        String requestBody = "";
        try {
            JSONObject statusUpdateJson = new JSONObject();
            String statusString = getRequestStatusString(statusToUpdate);
            statusUpdateJson.put("status", statusString);
            requestBody = statusUpdateJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    private NetworkObject createGetRequestsNetworkObject() {
        NetworkObject networkObject = new NetworkObject(GET_ORDERS_URL, HttpMethodType.GET);
        networkObject.setAuthToken(ServiceLocator.get(FacebookService.class).getAuthToken());
        return networkObject;
    }

    private void parseOrdersFromResponse(String response) throws JSONException {
        mUserOrders.clear();
        JSONArray ordersJsonArray = new JSONArray(response);
        for (int i = 0; i < ordersJsonArray.length(); i++) {
            JSONObject orderJson = ordersJsonArray.getJSONObject(i);
            Request request = new Request();
            request.setId(orderJson.getLong("id"));
            request.setSingleRequests(getSingleRequestsFromOrderJson(orderJson));
            request.setInitDate(getInitDateFromOrderJson(orderJson));
            request.setStatus(getRequestStatusFromOrderJson(orderJson));

            mUserOrders.add(request);
        }
    }

    private List<SingleRequest> getSingleRequestsFromOrderJson(JSONObject orderJson) throws JSONException {
        List<SingleRequest> singleRequests = new ArrayList<>();
        JSONArray singleRequestsJsonArray = orderJson.getJSONArray("singleRequests");
        for (int i = 0; i < singleRequestsJsonArray.length(); i++) {
            JSONObject singleRequestJson = singleRequestsJsonArray.getJSONObject(i);
            SingleRequest singleRequest = new SingleRequest(singleRequestJson.getLong("id"));
            singleRequest.setPlate(getPlateFromSingleRequestJson(singleRequestJson));
            singleRequest.setClarification(singleRequestJson.getString("comment"));
            singleRequest.setQuantity(singleRequestJson.getInt("quantity"));
            singleRequests.add(singleRequest);
        }
        return singleRequests;
    }

    private Date getInitDateFromOrderJson(JSONObject orderJson) throws JSONException {
        Long initDateInMilliseconds = orderJson.getLong("initAt");
        return new Date(initDateInMilliseconds);
    }

    private RequestStatus getRequestStatusFromOrderJson(JSONObject orderJson) throws  JSONException {
        String statusString = orderJson.getString("status");
        return RequestStatus.fromString(statusString);
    }

    private Plate getPlateFromSingleRequestJson(JSONObject singleRequestJson) throws JSONException {
        JSONObject plateJson = singleRequestJson.getJSONObject("plate");
        Plate plate = new Plate(plateJson.getLong("id"));
        plate.setName(plateJson.getString("name"));
        plate.setPrice(plateJson.getInt("price"));
        plate.setDescription(plateJson.getString("description"));
        plate.setSuitableForCeliac(plateJson.getBoolean("glutenFree"));
        plate.setCategories(getCategoriesFromPlateJson(plateJson));
        plate.setExtras(getExtrasFromPlateJson(plateJson));

        return plate;
    }

    private List<Category> getCategoriesFromPlateJson(JSONObject plateJson) throws JSONException {
        JSONObject plateCategoryJson = plateJson.getJSONObject("category");
        Category plateCategory = new Category(plateCategoryJson.getLong("id"), plateCategoryJson.getString("name"));
        List<Category> categories = new ArrayList<>();
        categories.add(plateCategory);
        return categories;
    }

    private List<Extra> getExtrasFromPlateJson(JSONObject plateJson) throws  JSONException {
        List<Extra> extras = new ArrayList<>();
        JSONArray plateExtrasJson = plateJson.getJSONArray("optionals");
        for (int i = 0; i < plateExtrasJson.length(); i++) {
            JSONObject extraJson = plateExtrasJson.getJSONObject(i);
            Extra extra = new Extra(extraJson.getLong("id"));
            extra.setName(extraJson.getString("name"));
            extra.setPrice(extraJson.getDouble("price"));
        }
        return extras;
    }

    private String getRequestStatusString(RequestStatus status) {
        switch (status) {
            case WaitingConfirmation: return "WAITING_CONFIRMATION";
            case OnPreparation: return  "ON_PREPARATION";
            case OnTheWay: return "ON_THE_WAY";
            case Delivered: return "DELIVERED";
            case CanceledByUser: return  "CANCELLED_BY_USER";
            case CanceledByCommerce: return "CANCELLED_BY_COMMERCE";
            default: return "";
        }
    }
}
