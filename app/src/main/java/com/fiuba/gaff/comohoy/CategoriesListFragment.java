package com.fiuba.gaff.comohoy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.fiuba.gaff.comohoy.adapters.CategoriesListAdapter;
import com.fiuba.gaff.comohoy.model.Categorie;
import com.fiuba.gaff.comohoy.services.CategorieService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;

public class CategoriesListFragment extends Fragment {

    private CategoriesListListener mCategoriesListListener;

    public interface CategoriesListListener {
        void onCategoriesClicked(Categorie categorie);
    }

    public CategoriesListFragment() {
    }

    public static CategoriesListFragment newInstance(int columnCount) {
        CategoriesListFragment fragment = new CategoriesListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View view = container.getChildAt(0);
        View view = inflater.inflate(R.layout.dialog_comidas, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            CategorieService categoriasService = getCategorieService();
            recyclerView.setAdapter(new CategoriesListAdapter(categoriasService.getCategories(), mCategoriesListListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoriesListListener) {
            mCategoriesListListener = ( CategoriesListListener ) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CategorieListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCategoriesListListener = null;
    }

    private CategorieService getCategorieService() {
        return ServiceLocator.get(CategorieService.class);
    }
}
