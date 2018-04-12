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

import com.fiuba.gaff.comohoy.adapters.CommerceListAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link CommerceListListener}
 * interface.
 */
public class CommercesListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private CommerceListListener mCommerceListListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CommerceListListener {
        void onCommerceClicked(Commerce commerce, View commerceTittleTextView);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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

        showProgressBar();
        loadCommerces();

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
        mRecyclerView.setAdapter(new CommerceListAdapter(commercesService.getCommerces(), mCommerceListListener));
        // after commerces are loaded
        showCommercesList();
    }

    private CommercesService getCommercesService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private void showCommercesList() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}