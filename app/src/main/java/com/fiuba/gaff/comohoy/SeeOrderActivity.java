package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.SingleRequestAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Opinion;
import com.fiuba.gaff.comohoy.model.purchases.RequestStatus;
import com.fiuba.gaff.comohoy.model.purchases.backend.Request;
import com.fiuba.gaff.comohoy.model.purchases.backend.SingleRequest;
import com.fiuba.gaff.comohoy.services.PurchasesService.OnRequestUpdatedCallback;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.opinions.OpinionsService;
import com.fiuba.gaff.comohoy.services.opinions.PublishOpinionCallback;

import java.util.List;

public class SeeOrderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Request mRequest;
    private Long mRequestId = -1L;

    private Dialog mCalificationsDialog;
    private OnRequestUpdatedCallback mOnRequestUpdatedCallback;

    private int mPuntuacion = 0;
    private String mCalificacion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_see_request);

        mRecyclerView = findViewById(R.id.single_request_list);

        obtainRequestId(savedInstanceState);
        mRequest = getPurchaseService().getRequestWithId(mRequestId);

        mOnRequestUpdatedCallback = getOnRequestUpdatedCallback();
        createCalificationDialog();

        setUpCommerceTitle();
        setUpOrderPrice();

        loadSingleRequestsList(mRequest.getSingleRequests());

        setUpActionButton(this, mRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(getString(R.string.intent_data_request_id), mRequestId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRequestId = savedInstanceState.getLong(getString(R.string.intent_data_request_id), -1);
    }

    private void setUpCommerceTitle() {
        TextView commerceTitle = findViewById(R.id.commerce_title);
        Commerce commerce = getCommerceService().getCommerce(mRequest.getCommerceId());
        if (commerce != null) {
            String commerceName = getCommerceService().getCommerce(mRequest.getCommerceId()).getShowableName();
            commerceTitle.setText(commerceName);
        } else {
            commerceTitle.setVisibility(View.GONE);
        }
    }

    private void setUpOrderPrice() {
        TextView orderPriceText = findViewById(R.id.textView_costo_total);
        orderPriceText.setText(String.format("Total: $%.2f", mRequest.getPrice()));
    }

    private void loadSingleRequestsList(List<SingleRequest> orders) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SingleRequestAdapter(orders));
    }

    private void obtainRequestId(Bundle savedInstanceState) {
        if (mRequestId.equals(-1L)) {
            Bundle extras = getIntent().getExtras();
            mRequestId = extras.getLong(getString(R.string.intent_data_request_id), -1L);
        }
        if ((mRequestId.equals(-1L)) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_commerce_id)))) {
            mRequestId = savedInstanceState.getLong(getString(R.string.intent_data_request_id));
        }
    }


    private void setUpActionButton(final Context context, final Request request) {
        AppCompatButton button = findViewById(R.id.button_action);
        final RequestStatus status = request.getStatus();
        switch (status) {
            case WaitingConfirmation:
                button.setText("CANCELAR PEDIDO");
                ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeleteConfirmationDialog(context, request);
                    }
                });
                break;
            case Delivered:
                button.setText("CALIFICAR PEDIDO");
                ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRateOrderClicked(request);
                    }
                });
                break;
            default:
                button.setText("VOLVER");
                ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(context.getResources().getColor(android.R.color.darker_gray)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
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
                cancelOrder(request.getId(), RequestStatus.CanceledByUser, mOnRequestUpdatedCallback);
                confirmationDialog.dismiss();
            }
        });

        confirmationDialog.show();
    }

    private void cancelOrder(Long orderId, RequestStatus status, OnRequestUpdatedCallback callback) {
        getPurchaseService().updateOrder(orderId, RequestStatus.CanceledByUser, this, mOnRequestUpdatedCallback);
    }

    private OnRequestUpdatedCallback getOnRequestUpdatedCallback() {
        OnRequestUpdatedCallback callback = new OnRequestUpdatedCallback() {
            @Override
            public void onSuccess() {
                showToast("Su pedido ha sido cancelado con éxito");
                finish();
            }

            @Override
            public void onError(String reason) {
                showToast("No se pudo modificar su pedido. Intente mas tarde");
            }
        };
        return callback;
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }

    public void onRateOrderClicked(final Request request) {
        mCalificationsDialog.show();
    }

    private void createCalificationDialog() {
        mCalificationsDialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        mCalificationsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCalificationsDialog.setContentView(R.layout.dialog_califications);
        mCalificationsDialog.setCanceledOnTouchOutside(false);
        //mCalificationsDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mCalificationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mCalificationsDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final EditText calificationEditText = mCalificationsDialog.findViewById(R.id.editText_califications);
        calificationEditText.setText("");

        LinearLayout ll = mCalificationsDialog.findViewById(R.id.puntuacion_comercio);
        final ImageView e1 = ll.findViewById(R.id.estrella1);
        final ImageView e2 = ll.findViewById(R.id.estrella2);
        final ImageView e3 = ll.findViewById(R.id.estrella3);
        final ImageView e4 = ll.findViewById(R.id.estrella4);
        final ImageView e5 = ll.findViewById(R.id.estrella5);

        e1.setClickable(true);
        e2.setClickable(true);
        e3.setClickable(true);
        e4.setClickable(true);
        e5.setClickable(true);

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e2.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                e3.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                e4.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                e5.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                mPuntuacion = 1;
            }
        });
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e2.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e3.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                e4.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                e5.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                mPuntuacion = 2;
            }
        });
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e2.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e3.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e4.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                e5.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                mPuntuacion = 3;
            }
        });
        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e2.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e3.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e4.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e5.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.whitestar));
                mPuntuacion = 4;
            }
        });
        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e2.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e3.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e4.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                e5.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.yellowstar));
                mPuntuacion = 5;
            }
        });

        Button acceptButton = mCalificationsDialog.findViewById(R.id.button_accept_calification);
        Button cancelButton = mCalificationsDialog.findViewById(R.id.button_cancel_calification);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalificacion = calificationEditText.getText().toString();
                Opinion opinion = new Opinion(0L);
                opinion.setDescription(mCalificacion);
                opinion.setOrderId(mRequest.getId());
                opinion.setPuntuation(mPuntuacion);
                sendOpinionToBackoffice(opinion);
                mCalificationsDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalificationsDialog.dismiss();
            }
        });
    }

    private void sendOpinionToBackoffice(Opinion opinion) {
        OpinionsService opinionsService = ServiceLocator.get(OpinionsService.class);
        opinionsService.publishOpinion(this, opinion, new PublishOpinionCallback() {
            @Override
            public void onSuccess() {
                showToast("Gracias por su opinión!");
            }

            @Override
            public void onError(String reason) {
                showToast("No se pudo publicar su calificación. Intente más tarde");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
