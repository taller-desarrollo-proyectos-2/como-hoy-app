package com.fiuba.gaff.comohoy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.CommerceDetailsActivity;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Plate;

import java.util.List;
import java.util.Locale;

public class MenuItemListAdapter extends RecyclerView.Adapter<MenuItemListAdapter.MenuItemViewHolder> {

    private final CommerceDetailsActivity.MenuListListener mMenuListListener;
    private List<Plate> mMenuItems;

    public MenuItemListAdapter(List<Plate> menuItems, CommerceDetailsActivity.MenuListListener listener){
        mMenuItems = menuItems;
        mMenuListListener = listener;
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final ImageView mPicture;
        private final TextView mPlateName;
        private final TextView mDescription;
        private final TextView mPrice;

        MenuItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCardView = (CardView) itemView.findViewById(R.id.card_view_menu_item);
            mPicture = (ImageView) itemView.findViewById(R.id.imagenPlato);
            mPlateName = (TextView) itemView.findViewById(R.id.textview_plate_name);
            mDescription = (TextView) itemView.findViewById(R.id.text_view_plate_desc);
            mPrice = (TextView) itemView.findViewById(R.id.text_view_order_status);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plate_item, parent, false);
        MenuItemViewHolder menuItemViewHolder = new MenuItemViewHolder(v);
        return menuItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final MenuItemViewHolder holder, int position) {
        final Plate plate = mMenuItems.get(position);
        Drawable platePictureDrawable = getPlatePictureDrawable(plate.getPicture(), holder.mView.getContext());
        holder.mPicture.setImageDrawable(platePictureDrawable);
        holder.mPlateName.setText(plate.getName());
        holder.mDescription.setText(plate.getDescription());
        String price = String.format(Locale.ENGLISH,"$%.2f", plate.getPrice());
        holder.mPrice.setText(price);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuListListener.onPlateClicked(plate);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    private Drawable getPlatePictureDrawable (Bitmap pictureBitmap, Context context) {
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
