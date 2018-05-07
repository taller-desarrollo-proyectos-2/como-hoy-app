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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.CategoriesAdapter;
import com.fiuba.gaff.comohoy.adapters.CommerceListAdapter;
import com.fiuba.gaff.comohoy.filters.CategoryFilter;
import com.fiuba.gaff.comohoy.filters.SearchFilter;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.CategoryUsageData;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.model.purchases.PaymentMethod;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.commerces.SortCriteria;
import com.fiuba.gaff.comohoy.services.commerces.UpdateCommercesCallback;
import com.fiuba.gaff.comohoy.services.location.LocationService;

import java.util.List;

public class CommercesListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mFiltersButton;
    private View mCategoriesButton;
    private SearchView mSearchView;


    private Category electedCategory;

    private GridView mCategoriesGridView;
    private FrameLayout mCategorie;
    private TextView mCategorieName;

    private SortCriteria mSortCriteria = SortCriteria.Closeness;
    private Dialog mCategoriesDialog;
    private Dialog mFiltersDialog;

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
        mFiltersButton = view.findViewById(R.id.action_button_filter);
        mCategoriesButton = view.findViewById(R.id.action_button_categories);
        mSearchView = view.findViewById(R.id.searchView);

        mCategorieName = view.findViewById(R.id.id_nombre_de_categoria);
        mCategorie = view.findViewById(R.id.categoria_fl);
        electedCategory = null;

        showProgress(true);

        Location currentLocation = getLocationService().getLocation(getActivity());
        //getCommercesService().updateCommercesWithLocation(getActivity(), createOnUpdatedCommercesCallback(), currentLocation);
        getCommercesService().updateCommercesData(getActivity(), createOnUpdatedCommercesCallback());

        createFiltersDialog();
        setUpSearchView();
        setUpFilterButton(view);
        setUpCategoriButton(view);
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

    private void setUpSearchView() {
        mSearchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                filterCommercesByText(newText, false);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCommercesByText(query, true);
                return true;
            }
        });
    }

    private void filterCommercesByText(String searchText, boolean notifyNoneCommerces) {
        List<Commerce> commerces = getCommercesService().getCommerces();
        if (commerces != null) {
            SearchFilter saerchFilter = new SearchFilter(searchText);
            List<Commerce> filteredCommerces = saerchFilter.apply(commerces);
            loadCommerces(filteredCommerces, notifyNoneCommerces);
        }
    }

    private void loadCommerces(List<Commerce> commerces, boolean notifyNoneCommerces) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new CommerceListAdapter(commerces, mCommerceListListener));
        if (notifyNoneCommerces && commerces.size() < 1) {
            Toast.makeText(getContext(), "No contamos con comercios en este momento", Toast.LENGTH_LONG).show();
        }
    }

    private CommercesService getCommercesService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private LocationService getLocationService() {
        return ServiceLocator.get(LocationService.class);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        mFiltersButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mCategoriesButton.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private UpdateCommercesCallback createOnUpdatedCommercesCallback() {
        return new UpdateCommercesCallback() {
            @Override
            public void onCommercesUpdated() {
                loadCommerces(getCommercesService().getCommercesSortedBy(getActivity(), mSortCriteria), true);
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
                mFiltersDialog.show();
            }
        });
    }
    private void setUpCategoriButton(View fragmentView){
        FloatingActionButton filterButton =  fragmentView.findViewById(R.id.action_button_categories);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCategoriesDialog == null) {
                    createCategoriesDialog();
                }
                mCategoriesDialog.show();
            }
        });
    }

    private void createCategoriesDialog() {
        mCategoriesDialog = new Dialog(getContext());
        mCategoriesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCategoriesDialog.setContentView(R.layout.dialog_categories);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mCategoriesDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        List<CategoryUsageData> categoriesUsageData = getCommercesService().getUsedCategoriesUsageData();
        GridView gridview = mCategoriesDialog.findViewById(R.id.gridCategories);
        if (categoriesUsageData != null && gridview != null){
            gridview.setAdapter(new CategoriesAdapter(getActivity(), categoriesUsageData, new CategoriesAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(String categoryName) {
                    CategoryFilter categoryFilter = new CategoryFilter(categoryName);
                    getCommercesService().addFilter(categoryFilter);
                    List<Commerce> filteredCommerces = getCommercesService().getCommerces();
                    loadCommerces(filteredCommerces, true);
                    mCategoriesDialog.dismiss();
                }
            }));
        }

        ImageView volver = window.findViewById(R.id.salirdialogo);
        volver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCategoriesDialog.dismiss();
            }
        });

        final ImageView quitar = window.findViewById(R.id.quitardialogo);
        quitar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                electedCategory = null;
                quitar.setVisibility(View.GONE);
                mCategoriesDialog.dismiss();
            }
        });

        //window.setContentView(mSVCategories);
    }

    private void createFiltersDialog() {
        mFiltersDialog = new Dialog(getContext());
        mFiltersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mFiltersDialog.setContentView(R.layout.dialog_filters);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mFiltersDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        Spinner spinner = mFiltersDialog.findViewById(R.id.spinner_sort_criteria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_criteria_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selectedItem = adapterView.getItemAtPosition(pos).toString();
                switch (selectedItem) {
                    case "Cercanía":
                        mSortCriteria = SortCriteria.Closeness;
                        break;
                    default: mSortCriteria = SortCriteria.Closeness;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button acceptButton = mFiltersDialog.findViewById(R.id.button_accept);
        Button cancelButton = mFiltersDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Commerce> sortedCommerces = getCommercesService().getCommercesSortedBy(getActivity(), mSortCriteria);
                loadCommerces(sortedCommerces, false);
                mFiltersDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFiltersDialog.dismiss();
            }
        });
    }
}

