package com.fiuba.gaff.comohoy.adapters;

/**
 * Created by Gonzalo on 28/05/2018.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.CartActivity;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.model.purchases.backend.SingleRequest;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.List;
import java.util.Locale;

public class SingleRequestAdapter extends RecyclerView.Adapter<SingleRequestAdapter.SingleRequestsViewHolder>  {

    private List<SingleRequest> mSingleRequests;

    public SingleRequestAdapter(List<SingleRequest> singleRequests){
        mSingleRequests = singleRequests;
    }

    public static class SingleRequestsViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final TextView mQuantity;
        private final TextView mPlateName;
        private final TextView mPrice;

        SingleRequestsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCardView = itemView.findViewById(R.id.card_view_plate_order_list_item);
            mQuantity = itemView.findViewById(R.id.textView_cantidad);
            mPlateName = itemView.findViewById(R.id.textView_plate_name);
            mPrice = itemView.findViewById(R.id.textView_price_value);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SingleRequestAdapter.SingleRequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_plate_order, parent, false);
        SingleRequestAdapter.SingleRequestsViewHolder platesViewHolder = new SingleRequestAdapter.SingleRequestsViewHolder(v);
        return platesViewHolder;
    }

    @Override
    public void onBindViewHolder(final SingleRequestAdapter.SingleRequestsViewHolder holder, int position) {
        final SingleRequest singleRequest = mSingleRequests.get(position);
        final Plate plate = singleRequest.getPlate();

        holder.mPlateName.setText(plate.getName());
        holder.mQuantity.setText(String.format(Locale.ENGLISH, "%d", singleRequest.getQuantity()));

        double extrasPrice = 0;
        for (Extra extra : singleRequest.getExtras()) {
            extrasPrice += extra.getPrice();
        }
        double platePrice = extrasPrice + plate.getPrice();
        holder.mPrice.setText(String.format(Locale.ENGLISH, "$%.2f", platePrice));
    }

    @Override
    public int getItemCount() {
        return mSingleRequests.size();
    }
}
