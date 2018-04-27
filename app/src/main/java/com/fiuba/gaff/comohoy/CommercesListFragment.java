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
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.CommerceListAdapter;
import com.fiuba.gaff.comohoy.filters.RatingFilter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.commerces.SortCriteria;
import com.fiuba.gaff.comohoy.services.commerces.UpdateCommercesCallback;

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

        setUpFilterButton(view);

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

    private void loadCommerces(List<Commerce> commerces) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                loadCommerces(getCommercesService().getCommercesSortedBy(getActivity(), SortCriteria.Closeness));
                showProgress(false);
            }

            @Override
            public void onError(String reason) {
                Toast.makeText(getContext(), reason, Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        };
    }

    private void setUpFilterButton(View fragmentView) {
        FloatingActionButton filterButton =  fragmentView.findViewById(R.id.action_button_filter);
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
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);

                Button filterButton= (Button) dialog.findViewById(R.id.button_confirm_filtrar);
                filterButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(((CheckBox) dialog.findViewById(R.id.id_filtro_puntaje)).isChecked()){
                            List<Commerce> commerces = getCommercesService().getCommerces();

                            EditText ratingFilterEditText = dialog.findViewById(R.id.edittext_puntaje_ingresado);
                            String enteredValue = ratingFilterEditText.getText().toString();
                            double puntajeFiltrar = 0;
                            if(enteredValue != null && !enteredValue.isEmpty()) {
                                puntajeFiltrar = Double.parseDouble(ratingFilterEditText.getText().toString());
                            } else {
                                String hintText = ratingFilterEditText.getHint().toString();
                                if (!hintText.isEmpty()){
                                    puntajeFiltrar = Double.parseDouble(hintText);
                                }
                            }

                            RatingFilter ratingFilter = new RatingFilter(puntajeFiltrar);
                            List<Commerce> filteredList = ratingFilter.apply(commerces);

                            loadCommerces(filteredList);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
