package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.CommerceListAdapter;
import com.fiuba.gaff.comohoy.filters.CalificationFilter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.commerces.UpdateCommercesCallback;

import java.util.ArrayList;
import java.util.List;

public class CommercesListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private CommerceListListener mCommerceListListener;

    public interface CommerceListListener {
        void onCommerceClicked(Commerce commerce, View commerceTitleTextView);
    }

    public CommercesListFragment() {
    }

    public static CommercesListFragment newInstance(int columnCount) {
        CommercesListFragment fragment = new CommercesListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview_commerces_list);
        mProgressBar = view.findViewById(R.id.progress_bar_commerces_list);

        showProgress(true);

        getCommercesService().updateCommercesData(getActivity(), createOnUpdatedCommercesCallback());

        FloatingActionButton filterButton =  view.findViewById(R.id.filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filters);
                dialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

                Button filterbutton= (Button) dialog.findViewById(R.id.button_confirm_filtrar);
                filterbutton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //CheckBox filtroPuntaje = (CheckBox) v.findElementById(R.id.id_filtro_puntaje);
                        if(((CheckBox) dialog.findViewById(R.id.id_filtro_puntaje)).isChecked()){

                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            CommercesService commercesService = getCommercesService();
                            List<Commerce> commerces = commercesService.getCommerces();
                            EditText strpuntajeFiltrar = (EditText) dialog.findViewById(R.id.puntajeafiltrar);
                            float puntajeFiltrar = Float.valueOf(strpuntajeFiltrar.getText().toString());

                            CalificationFilter filtroCalificaciones = new CalificationFilter(commerces,puntajeFiltrar);
                            List<Commerce> listaComercioFiltrada = filtroCalificaciones.apply(commerces);

                            mRecyclerView.setAdapter(new CommerceListAdapter(listaComercioFiltrada, mCommerceListListener));
                            if (commerces.size() < 1) {
                                Toast.makeText(getContext(), "No contamos con comercios en este momento", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommerceListListener) {
            mCommerceListListener = (CommerceListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CommerceListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommerceListListener = null;
    }

    private void loadCommerces() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommercesService commercesService = getCommercesService();
        List<Commerce> commerces = commercesService.getCommerces();
        mRecyclerView.setAdapter(new CommerceListAdapter(commerces, mCommerceListListener));
        if (commerces.size() < 1) {
            Toast.makeText(getContext(), "No contamos con comercios en este momento", Toast.LENGTH_LONG).show();
        }
    }

    private CommercesService getCommercesService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private UpdateCommercesCallback createOnUpdatedCommercesCallback() {
        return new UpdateCommercesCallback() {
            @Override
            public void onCommercesUpdated() {
                loadCommerces();
                showProgress(false);
            }

            @Override
            public void onError(String reason) {
                Toast.makeText(getContext(), reason, Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        };
    }
}
