package com.fiuba.gaff.comohoy.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.CategoryUsageData;
import com.fiuba.gaff.comohoy.utils.CategoriesUtils;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {

    private Activity mActivityContext;
    private List<CategoryUsageData> mCategoriesUsageData;

    public CategoriesAdapter(Activity activityContext, List<CategoryUsageData> categoriesUsageData) {
        mActivityContext = activityContext;
        mCategoriesUsageData = categoriesUsageData;
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

        View grid;

        if(convertView == null){
            grid = new View(mActivityContext);
            LayoutInflater inflater = mActivityContext.getLayoutInflater();
            grid = inflater.inflate(R.layout.categories_grid_layout_item, parent, false);
        }else{
            grid = convertView;
        }

        CategoryUsageData categoryUsageData = mCategoriesUsageData.get(position);
        String categoryName = categoryUsageData.getCategory().getName();
        int categoryDrawableId = CategoriesUtils.getCategoryDrawableIdByName(categoryName, mActivityContext);
        ImageView imageView = grid.findViewById(R.id.image);
        imageView.setImageResource(categoryDrawableId);

        return grid;
    }

}
