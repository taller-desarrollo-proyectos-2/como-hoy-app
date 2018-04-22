package com.fiuba.gaff.comohoy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.fiuba.gaff.comohoy.adapters.InformationPlateAdapter;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.InformationPlateService;
//import com.fiuba.gaff.comohoy.services.commerces.UpdateCommercesCallback;
import java.util.List;

public class InformationPlateFragment extends Fragment {

    //private RecyclerView mRecyclerView;
    private InformationPlateListener mInformationPlateListener;

    public interface InformationPlateListener {
        void onInformationPlateClicked(Plate plate, View informationPlateTitleTextView);
    }

    public InformationPlateFragment() {
    }

    public static InformationPlateFragment newInstance(int columnCount) {
        InformationPlateFragment fragment = new InformationPlateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information_plate, container, false);
        //mRecyclerView = view.findViewById(R.id.recyclerview_commerces_list);
        getInformationPlateService();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InformationPlateListener) {
            mInformationPlateListener= (InformationPlateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CommerceListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInformationPlateListener = null;
    }
/*
    private void loadCommerces() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommercesService commercesService = getCommercesService();
        List<Commerce> commerces = commercesService.getCommerces();
        mRecyclerView.setAdapter(new CommerceListAdapter(commerces, mCommerceListListener));
        if (commerces.size() < 1) {
            Toast.makeText(getContext(), "No contamos con comercios en este momento", Toast.LENGTH_LONG).show();
        }
    }
*/
    private InformationPlateService getInformationPlateService() {
        return ServiceLocator.get(InformationPlateService.class);
    }

}
