package com.fiuba.gaff.comohoy.adapters;


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
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.List;
import java.util.Locale;

public class PlatesOrderAdapter extends RecyclerView.Adapter<PlatesOrderAdapter.PlateOrdersViewHolder>  {
    private final CartActivity.PlatesOrderListListener mPlatesOrderListListener;
    private List<PlateOrder> mPlateOrders;

    public PlatesOrderAdapter(List<PlateOrder> plateOrders, CartActivity.PlatesOrderListListener listener){
        mPlateOrders = plateOrders;
        mPlatesOrderListListener = listener;
    }

    public static class PlateOrdersViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final TextView mQuantity;
        private final TextView mPlateName;
        private final TextView mPrice;
        private final ImageButton mDeleteButton;

        PlateOrdersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCardView = itemView.findViewById(R.id.card_view_plate_order_list_item);
            mQuantity = itemView.findViewById(R.id.textView_cantidad);
            mPlateName = itemView.findViewById(R.id.textView_plate_name);
            mPrice = itemView.findViewById(R.id.textView_price_value);
            mDeleteButton = itemView.findViewById(R.id.button_remove);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PlatesOrderAdapter.PlateOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_plate_order, parent, false);
        PlatesOrderAdapter.PlateOrdersViewHolder platesViewHolder = new PlatesOrderAdapter.PlateOrdersViewHolder(v);
        return platesViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlatesOrderAdapter.PlateOrdersViewHolder holder, int position) {
        final PlateOrder plateOrder = mPlateOrders.get(position);
        final Commerce commerce = getCommerceService().getCommerce(plateOrder.getCommerceId());
        final Plate plate = commerce.getPlate(plateOrder.getPlateId());

        holder.mPlateName.setText(plate.getName());
        holder.mQuantity.setText(String.format(Locale.ENGLISH, "%d",plateOrder.getQuantity()));
        holder.mPrice.setText(String.format(Locale.ENGLISH, "$%.2f", plateOrder.getOrderPrice()));

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlatesOrderListListener.onPlateOrderClicked(plateOrder);
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(view.getContext(), plateOrder, plate, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlateOrders.size();
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private void showDeleteConfirmationDialog(Context context, final PlateOrder plateOrder, Plate plate, final int position) {
        final Dialog confirmationDialog = new Dialog(context, android.R.style.Theme_Holo_Light_Dialog);
        confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmationDialog.setContentView(R.layout.dialog_confirmation);
        confirmationDialog.setCanceledOnTouchOutside(false);
        confirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        TextView messageTextView = confirmationDialog.findViewById(R.id.textView_message);
        messageTextView.setText(String.format("¿Estás seguro que deseas eliminar de tu pedido: %d %s?",plateOrder.getQuantity(), plate.getName()));

        Button cancelButton = confirmationDialog.findViewById(R.id.button_cancel);
        Button acceptButton = confirmationDialog.findViewById(R.id.button_accept);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlateOrder(plateOrder, position);
                mPlatesOrderListListener.onPlateOrderRemoved();
                confirmationDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });
        confirmationDialog.show();
    }

    private void removePlateOrder(PlateOrder plateOrder, int position) {
        getPurchaseService().removePlateOrder(plateOrder.getOrderId());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mPlateOrders.size());
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }
}
