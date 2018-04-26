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

import com.fiuba.gaff.comohoy.CommercesListFragment;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.List;

public class CommerceListAdapter extends RecyclerView.Adapter<CommerceListAdapter.CommerceViewHolder> {

    private final CommercesListFragment.CommerceListListener mCommerceListListener;
    private List<Commerce> mCommerces;

    public CommerceListAdapter(List<Commerce> commerces, CommercesListFragment.CommerceListListener listener){
        mCommerces = commerces;
        mCommerceListListener = listener;
    }

    public static class CommerceViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final ImageView mPicture;
        private final TextView mName;
        private final TextView mDescription;
        private final TextView mRating;
        private final TextView mOrdersAmount;
        private final TextView mShippingTime;
        private final TextView mShippingCost;
        private final TextView mDiscounts;

        CommerceViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCardView = (CardView) itemView.findViewById(R.id.card_view_comercio_list_item);
            mPicture = (ImageView) itemView.findViewById(R.id.imagenComercio);
            mName = (TextView) itemView.findViewById(R.id.nombreComercio);
            mDescription = (TextView) itemView.findViewById(R.id.descripcionComercio);
            mRating = (TextView) itemView.findViewById(R.id.puntajeComercio);
            mOrdersAmount = (TextView) itemView.findViewById(R.id.cantPedidosComercio);
            mShippingTime = (TextView) itemView.findViewById(R.id.tiempoEnvioComercio);
            mShippingCost = (TextView) itemView.findViewById(R.id.costoEnvioComercio);
            mDiscounts = (TextView) itemView.findViewById(R.id.descuento_comercio);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CommerceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_comercio, parent, false);
        CommerceViewHolder commerceViewHolder = new CommerceViewHolder(v);
        return commerceViewHolder;
    }

    @Override
    public void onBindViewHolder(final CommerceViewHolder holder, int position) {
        final Commerce commerce = mCommerces.get(position);

        Bitmap commercePictureBitmap = commerce.getPicture();
        Drawable commercePictureDrawable = getCommercePictureDrawable(commercePictureBitmap, holder.mView.getContext());

        holder.mPicture.setImageDrawable(commercePictureDrawable);
        //holder.mPicture.setImageBitmap(commerce.getPicture());
        holder.mName.setText(commerce.getShowableName());
        holder.mDescription.setText(commerce.getDescription());
        holder.mRating.setText(String.format("%.1f", commerce.getRating()));
        holder.mOrdersAmount.setText(commerce.getOrdersAmount());
        holder.mShippingTime.setText(commerce.getShippingTime());
        holder.mShippingCost.setText(commerce.getShippingCost());

        String discounts = commerce.getDiscounts();
        if (discounts.equals("")) {
            holder.mDiscounts.setVisibility(View.GONE);
        }
        else {
            holder.mDiscounts.setVisibility(View.VISIBLE);
            holder.mDiscounts.setText(mCommerces.get(position).getDiscounts());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommerceListListener.onCommerceClicked(commerce, holder.mName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommerces.size();
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
