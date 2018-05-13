package com.fiuba.gaff.comohoy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.gaff.comohoy.model.purchases.backend.Request;
import com.fiuba.gaff.comohoy.services.PurchasesService.OnGetOrdersCallback;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;

import java.util.List;


public class MyOrdersFragment extends Fragment {

    public MyOrdersFragment() {
        // Required empty public constructor
    }

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

        getPurchaseService().getOrdersFromServer(getActivity(), new OnGetOrdersCallback() {
            @Override
            public void onSuccess(List<Request> orders) {

            }

            @Override
            public void onError(String reason) {

            }
        });

        return view;
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }

}
