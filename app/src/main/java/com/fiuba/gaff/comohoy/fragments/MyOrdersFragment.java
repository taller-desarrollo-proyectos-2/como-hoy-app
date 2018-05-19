package com.fiuba.gaff.comohoy.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.adapters.MyOrdersAdapter;
import com.fiuba.gaff.comohoy.model.purchases.RequestStatus;
import com.fiuba.gaff.comohoy.model.purchases.backend.Request;
import com.fiuba.gaff.comohoy.services.PurchasesService.OnGetOrdersCallback;
import com.fiuba.gaff.comohoy.services.PurchasesService.OnRequestUpdatedCallback;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;

import java.util.List;


public class MyOrdersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private OnRequestUpdatedCallback mOnRequestUpdatedCallback;
    private OrdersListListener mOrdersListListener;

    public interface OrdersListListener {
        void onOrderClicked(Request request);
        void onCancelOrder(Long orderId);
        void onRateOrderClicked(Request request);
    }

    public MyOrdersFragment() {}

    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // recover fragment state
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_orders, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview_orders_list);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mOnRequestUpdatedCallback = getOnRequestUpdatedCallback();
        mOrdersListListener = getOrdersListListener();

        showProgress(true);
        updateOrdersList();

        return view;
    }

    private void updateOrdersList() {
        getPurchaseService().getOrdersFromServer(getActivity(), new OnGetOrdersCallback() {
            @Override
            public void onSuccess(List<Request> orders) {
                loadOrders(orders);
                showProgress(false);
            }

            @Override
            public void onError(String reason) {
                Toast.makeText(getActivity(), "No se pudo obtener la información de sus pedidos. Intente más tarde", Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });
    }

    private void loadOrders(List<Request> orders) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new MyOrdersAdapter(orders, mOrdersListListener));
        if (orders.size() < 1) {
            Toast.makeText(getContext(), "No tiene ningún pedido", Toast.LENGTH_LONG).show();
        }
    }

    private OnRequestUpdatedCallback getOnRequestUpdatedCallback() {
        OnRequestUpdatedCallback callback = new OnRequestUpdatedCallback() {
            @Override
            public void onSuccess() {
                updateOrdersList();
            }

            @Override
            public void onError(String reason) {
                Toast.makeText(getActivity(), "No se pudo modificar su pedido. Intente mas tarde", Toast.LENGTH_LONG).show();
            }
        };
        return callback;
    }

    private OrdersListListener getOrdersListListener() {
        OrdersListListener listener = new OrdersListListener() {
            @Override
            public void onOrderClicked(Request request) {
                // TODO abrir pantalla para expandir información del pedido
            }

            @Override
            public void onCancelOrder(Long orderId) {
                getPurchaseService().updateOrder(orderId, RequestStatus.CanceledByUser, getActivity(), mOnRequestUpdatedCallback);
            }

            @Override
            public void onRateOrderClicked(Request request) {
                // TODO abrir dialog para calificar pedido
            }
        };
        return listener;
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }

}
