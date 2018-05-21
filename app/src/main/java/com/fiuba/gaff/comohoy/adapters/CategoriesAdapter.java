package com.fiuba.gaff.comohoy.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.CategoryUsageData;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.picasso.PicassoService;
import com.fiuba.gaff.comohoy.services.picasso.RoundedCornersTransform;
import com.fiuba.gaff.comohoy.utils.CategoriesUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {

    private Activity mActivityContext;
    private List<CategoryUsageData> mCategoriesUsageData;
    private CategoryClickListener mListener;

    public interface CategoryClickListener {
        void onCategoryClicked(String categoryName);
    }

    public CategoriesAdapter(Activity activityContext, List<CategoryUsageData> categoriesUsageData, CategoryClickListener listener) {
        mActivityContext = activityContext;
        mCategoriesUsageData = categoriesUsageData;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mCategoriesUsageData.size();
    }

    @Override
    public Object getItem(int index) {
        if (index < mCategoriesUsageData.size()) {
            return null;
        }
        return mCategoriesUsageData.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if(convertView == null){
            view = new View(mActivityContext);
            LayoutInflater inflater = mActivityContext.getLayoutInflater();
            view = inflater.inflate(R.layout.categories_grid_layout_item, parent, false);
        }else{
            view = convertView;
        }

        final CategoryUsageData categoryUsageData = mCategoriesUsageData.get(position);
        String categoryName = categoryUsageData.getCategory().getName();

        TextView textView = view.findViewById(R.id.nombre_categoria);
        textView.setText(categoryName);

        TextView commercesAmountTextView = view.findViewById(R.id.textView_commerces_amount);
        String commercesAmountFormat = (categoryUsageData.getUsesAmount() > 1) ? "%d comercios" : "%d comercio";
        commercesAmountTextView.setText(String.format(commercesAmountFormat, categoryUsageData.getUsesAmount()));

        ImageView imageView = view.findViewById(R.id.image);
        int categoryDrawableId = CategoriesUtils.getCategoryDrawableIdByName(categoryName, mActivityContext);
        Picasso picasso = ServiceLocator.get(PicassoService.class).getPicasso();
        picasso.load(categoryDrawableId).fit().centerInside().transform(new RoundedCornersTransform()).error(R.drawable.no_image).into(imageView);

        ViewGroup parentLayout = view.findViewById(R.id.parent_layout);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCategoryClicked(categoryUsageData.getCategory().getName());
            }
        });
        return view;
    }

}
