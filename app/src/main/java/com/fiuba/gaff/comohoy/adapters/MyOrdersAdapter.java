package com.fiuba.gaff.comohoy.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.fragments.MyOrdersFragment;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.model.purchases.RequestStatus;
import com.fiuba.gaff.comohoy.model.purchases.backend.Request;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.picasso.CircleTransform;
import com.fiuba.gaff.comohoy.services.picasso.PicassoService;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.OrderViewHolder>  {

    private final MyOrdersFragment.OrdersListListener mOrdersListListener;
    private List<Request> mOrders;

    public MyOrdersAdapter(List<Request> orders, MyOrdersFragment.OrdersListListener listener){
        mOrders = orders;
        mOrdersListListener = listener;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final TextView mInitDateTextView;
        private final ImageView mCommerceImage;
        private final TextView mOrderStatus;
        private final TextView mOrderPrice;
        //private final ImageButton mActionButton;

        OrderViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCardView = itemView.findViewById(R.id.card_view_order_item);
            mInitDateTextView = itemView.findViewById(R.id.textview_order_id_value);
            mCommerceImage = itemView.findViewById(R.id.imageview_commerce_image);
            mOrderStatus = itemView.findViewById(R.id.textView_order_status_value);
            mOrderPrice = itemView.findViewById(R.id.textView_order_price);
            //mActionButton = itemView.findViewById(R.id.button_status_action);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MyOrdersAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        MyOrdersAdapter.OrderViewHolder ordersViewHolder = new MyOrdersAdapter.OrderViewHolder(v);
        return ordersViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyOrdersAdapter.OrderViewHolder holder, int position) {
        final Request request = mOrders.get(position);
        // final Commerce commerce = getCommerceService().getCommerce(plateOrder.getCommerceId());
        // final Plate plate = commerce.getPlate(plateOrder.getPlateId());

        String initDateString = new SimpleDateFormat("dd-MM-yyyy").format(request.getInitDate());
        holder.mInitDateTextView.setText(initDateString);
        holder.mOrderStatus.setText(request.getStatus().toString());
        holder.mOrderPrice.setText(String.format("$%.0f", request.getPrice()));

        //int commerceId = request.getSingleRequests().get(0).getPlate();
        String uriFormat = "http://34.237.197.99:9000/api/v1/commerces/%d/picture";
        String uri = String.format(uriFormat, request.getCommerceId());
        Picasso picasso = ServiceLocator.get(PicassoService.class).getPicasso();
        picasso.load(uri).fit().transform(new CircleTransform()).placeholder(R.drawable.progress_animation).error(R.drawable.no_image).into(holder.mCommerceImage);

        //setUpActionButton(holder.mView.getContext(), holder.mActionButton, request);
        setUpCardView(holder.mCardView, request);
    }

    private void setUpCardView(CardView cardView, final Request request) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOrdersListListener.onOrderClicked(request);
            }
        });
    }

    private void setUpActionButton(final Context context, ImageButton imageButton, final Request request) {
        final RequestStatus status = request.getStatus();
        switch (status) {
            case WaitingConfirmation:
                // imageButton.setText("CANCELAR PEDIDO");
                //ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                imageButton.setImageResource(R.drawable.trashcan_icon);
                imageButton.setVisibility(View.VISIBLE);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeleteConfirmationDialog(context, request);
                    }
                });
                break;
            case Delivered:
                // imageButton.setText("CALIFICAR PEDIDO");
                // ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
                imageButton.setVisibility(View.VISIBLE);
                imageButton.setImageResource(R.drawable.star);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOrdersListListener.onRateOrderClicked(request);
                    }
                });
                break;
            default:
                imageButton.setVisibility(View.GONE);
        }
    }

    private void showDeleteConfirmationDialog(Context context, final Request request) {
        final Dialog confirmationDialog = new Dialog(context, android.R.style.Theme_Holo_Light_Dialog);
        confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmationDialog.setContentView(R.layout.dialog_confirmation_one_button);
        confirmationDialog.setCanceledOnTouchOutside(true);
        confirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView messageTextView = confirmationDialog.findViewById(R.id.textView_message);
        messageTextView.setText(String.format("¿Estás seguro que desea cancelar el pedido nº %d?", request.getId()));

        Button cancelButton = confirmationDialog.findViewById(R.id.button_cancel);
        cancelButton.setText("CANCELAR PEDIDO");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrdersListListener.onCancelOrder(request.getId());
                confirmationDialog.dismiss();
            }
        });

        confirmationDialog.show();
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

}