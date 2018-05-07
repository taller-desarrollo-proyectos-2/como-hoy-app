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
        TextView textView = grid.findViewById(R.id.nombre_categoria);


        imageView.setImageResource(categoryDrawableId);
        Drawable drawable =  imageView.getDrawable();
        imageView.setImageDrawable(getCategoriesPictureDrawable(drawableToBitmap(drawable),mActivityContext));
        textView.setText(categoryName);
        return grid;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Drawable getCategoriesPictureDrawable (Bitmap pictureBitmap, Context context) {
        Drawable drawable = null;
        if (pictureBitmap != null) {
            if (pictureBitmap.getWidth() > pictureBitmap.getHeight()) {
                pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getHeight(), pictureBitmap.getHeight());
            } else if (pictureBitmap.getWidth() < pictureBitmap.getHeight()) {
                pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getWidth(), pictureBitmap.getWidth());
            }
        }
        return  new BitmapDrawable(context.getResources(), pictureBitmap);
    }

    private Drawable getCommercePictureDrawable (Bitmap pictureBitmap, Context context) {
        RoundedBitmapDrawable roundedDrawable = null;
        if (pictureBitmap != null) {
            if (pictureBitmap.getWidth() > pictureBitmap.getHeight()) {
                pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getHeight(), pictureBitmap.getHeight());
            } else if (pictureBitmap.getWidth() < pictureBitmap.getHeight()) {
                pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getWidth(), pictureBitmap.getWidth());
            }
            roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), pictureBitmap);
            roundedDrawable.setCornerRadius(pictureBitmap.getHeight());
        }
        return roundedDrawable;
    }

}
