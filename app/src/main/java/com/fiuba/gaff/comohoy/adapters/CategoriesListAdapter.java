package com.fiuba.gaff.comohoy.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.CategoriesListFragment;
import com.fiuba.gaff.comohoy.model.Categorie;

import java.util.List;

//import static com.squareup.picasso.Utils.getResources;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.CategoriesViewHolder>  {

    private final CategoriesListFragment.CategoriesListListener mCategoriesListListener;

    private List<Categorie> mCategories;

    public CategoriesListAdapter(List<Categorie> categories, CategoriesListFragment.CategoriesListListener listener){
        mCategories = categories;
        mCategoriesListListener = listener;
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final Button mButton;
        //private final ImageView mPicture;
        //private final TextView mName;

        CategoriesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mButton = (Button) itemView.findViewById(R.id.button_categoria);
            //mPicture = (ImageView) itemView.findViewById(R.id.id_imagencontactonuevo);
            //mName = (TextView) itemView.findViewById(R.id.id_nomcontactonuevo);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorie, parent, false);
        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(v);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
        final Categorie categorie = mCategories.get(position);

        holder.mButton.setText(categorie.getName());
        Drawable d = new BitmapDrawable(holder.mView.getResources(), categorie.getPicture());
        holder.mButton.setBackground(d);

    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
