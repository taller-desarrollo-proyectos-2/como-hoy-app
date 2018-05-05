package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.adapters.ExtrasListAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class OrderPlateActivity extends AppCompatActivity {

    private int mCommerceId = -1;
    private Long mPlateId = -1L;
    private Long mPlateOrderId = -1L;
    private boolean mIsModifyingPlate = false;
    private PlateOrder mPlateOrder;

    private Dialog mQuantityDialog;
    private Dialog mExtrasDialog;
    private Dialog mClarificationsDialog;

    private double mOrderPrice = 0;
    private int mOrderQuantity = 1;
    private String mClarifications = "";
    private HashMap<Long, Extra> mExtrasAdded = new HashMap<>();

    public interface ExtrasListListener {
        void onExtraClicked(ExtrasListAdapter.ExtraItem extraItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plate);

        obtainCommerceId(savedInstanceState);
        obtainPlateId(savedInstanceState);
        obtainPlateOrderId(savedInstanceState);
        obtainIsModifyingPlate(savedInstanceState);

        if (mIsModifyingPlate) {
            mPlateOrder = getPlateOrder();
        }

        fillFieldsWithPlateData();

        setUpQuantityCard();
        setUpExtrasCard();
        setUpClarificationsCard();

        setUpAddPlateButton();

        updateOrderCost();
    }

    private void fillFieldsWithPlateData() {
        TextView plateNameTextField = findViewById(R.id.plate_name);
        TextView plateDescriptionTextField = findViewById(R.id.plate_description);
        TextView platePriceTextField = findViewById(R.id.plate_price);
        ImageView celiacImageView = findViewById(R.id.celiac_icon);

        Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        Plate plate = commerce.getPlate(mPlateId);
        plateNameTextField.setText(plate.getName());
        plateDescriptionTextField.setText(plate.getDescription());
        platePriceTextField.setText(String.format(Locale.ENGLISH,"$%.2f", plate.getPrice()));
        if (!plate.isSuitableForCeliac()) {
            celiacImageView.setVisibility(View.GONE);
        }
    }

    private void setUpAddPlateButton() {
        Button addPlateButton = findViewById(R.id.add_plate_button);
        String buttonText = mIsModifyingPlate ? "Modificar pedido" : "Agregar a Pedido";
        addPlateButton.setText(buttonText);
        addPlateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlateToCart();
                finish();
            }
        });
    }

    private void addPlateToCart() {
        PlateOrder plateOrder = new PlateOrder();
        plateOrder.setCommerceId(mCommerceId);
        plateOrder.setQuantity(mOrderQuantity);
        plateOrder.setExtrasId(new ArrayList<>(mExtrasAdded.keySet()));
        plateOrder.setClarifications(mClarifications);
        plateOrder.setOrderPrice(mOrderPrice);
        plateOrder.setPlateId(mPlateId);

        PurchasesService purchasesService = getPurchaseService();
        if (mIsModifyingPlate) {
            purchasesService.modifyPlateOrder(mPlateOrderId, plateOrder);
        } else {
            purchasesService.addPlateOrderToCart(plateOrder);
        }
    }

    private void setUpQuantityCard() {
        CardView quantityCardView = findViewById(R.id.cardview_cantidad);
        quantityCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuantityDialog();
            }
        });

        mOrderQuantity = mIsModifyingPlate ? mPlateOrder.getQuantity() : 1;
        updateQuantityValue();

        createQuantityDialog();
    }

    private void setUpExtrasCard() {
        CardView extrasCard = findViewById(R.id.cardview_extras);
        Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        Plate plate = commerce.getPlate(mPlateId);
        final List<Extra> extras = plate.getExtras();
        if (extras.size() > 0) {
            createExtrasDialog(extras);
            extrasCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openExtrasDialog();
                }
            });
            if (mIsModifyingPlate) {
                addExtrasFromExtrasIds(mPlateOrder.getExtrasId(), plate);
            }
            updateExtrasValue();
        } else {
            extrasCard.setVisibility(View.GONE);
        }
    }

    private void addExtrasFromExtrasIds(List<Long> extrasIds, Plate plate) {
        for (Long id : extrasIds) {
            if (plate.containsExtra(id)) {
                Extra extraToAdd = plate.getExtraWithId(id);
                mExtrasAdded.put(id, extraToAdd);
            }
        }
    }

    private void setUpClarificationsCard() {
        createClarificationsDialog();
        CardView clarificationsCardView = findViewById(R.id.cardview_aclaraciones);
        clarificationsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClarificationsDialog();
            }
        });
        mClarifications = mIsModifyingPlate ? mPlateOrder.getClarifications() : "";
        updateClarificationsValue();
    }

    private void createQuantityDialog() {
        mQuantityDialog = new Dialog(this, android.R.style.Theme_Dialog);
        mQuantityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mQuantityDialog.setContentView(R.layout.dialog_cantidad);
        mQuantityDialog.setCanceledOnTouchOutside(false);
        mQuantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mQuantityDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        final NumberPicker numberPicker = mQuantityDialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        numberPicker.setValue(mOrderQuantity);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

            }
        });

        Button acceptButton = mQuantityDialog.findViewById(R.id.button_accept);
        Button cancelButton = mQuantityDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderQuantity = numberPicker.getValue();
                updateQuantityValue();
                updateOrderCost();
                mQuantityDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantityDialog.dismiss();
            }
        });
    }

    private void createExtrasDialog(List<Extra> extras) {
        mExtrasDialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        mExtrasDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mExtrasDialog.setContentView(R.layout.dialog_extras);
        mExtrasDialog.setCanceledOnTouchOutside(false);
        mExtrasDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mExtrasDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        setExtrasListAdapter(extras);

        Button acceptButton = mExtrasDialog.findViewById(R.id.button_accept);
        Button cancelButton = mExtrasDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExtrasMap();
                updateExtrasValue();
                updateOrderCost();
                mExtrasDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExtrasDialog.dismiss();
            }
        });
    }

    private void updateExtrasMap() {
        final RecyclerView recyclerView = mExtrasDialog.findViewById(R.id.extrasList);
        ExtrasListAdapter extrasAdapter = (ExtrasListAdapter) recyclerView.getAdapter();
        List<ExtrasListAdapter.ExtraItem> extraItems = extrasAdapter.getExtras();
        for (ExtrasListAdapter.ExtraItem item : extraItems) {
            Extra extra = item.getExtra();
            if (item.isSelected()) {
                mExtrasAdded.put(extra.getId(), extra);
            } else {
                if (mExtrasAdded.containsKey(extra.getId())) {
                    mExtrasAdded.remove(extra.getId());
                }
            }
        }
    }

    private void createClarificationsDialog() {
        mClarificationsDialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        mClarificationsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mClarificationsDialog.setContentView(R.layout.dialog_clarifications);
        mClarificationsDialog.setCanceledOnTouchOutside(false);
        mClarificationsDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mClarificationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        mClarificationsDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final EditText clarificationsEditText = mClarificationsDialog.findViewById(R.id.editText_clarifications);
        clarificationsEditText.setText("");

        Button acceptButton = mClarificationsDialog.findViewById(R.id.button_accept);
        Button cancelButton = mClarificationsDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClarifications = clarificationsEditText.getText().toString();
                updateClarificationsValue();
                mClarificationsDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClarificationsDialog.dismiss();
            }
        });
    }

    private void openQuantityDialog() {
        mQuantityDialog.show();
    }

    private void openExtrasDialog() {
        updateExtrasListAdapterItems();
        mExtrasDialog.show();
    }

    private void openClarificationsDialog() {
        EditText clarificationsEditText = mClarificationsDialog.findViewById(R.id.editText_clarifications);
        clarificationsEditText.setText(mClarifications);
        clarificationsEditText.requestFocus();
        mClarificationsDialog.show();
    }

    private void setExtrasListAdapter(List<Extra> extras) {
        final RecyclerView recyclerView = mExtrasDialog.findViewById(R.id.extrasList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ExtrasListAdapter.ExtraItem> extraItems = new ArrayList<>();
        for (Extra extra : extras) {
            ExtrasListAdapter.ExtraItem extraItem = new ExtrasListAdapter.ExtraItem(extra);
            extraItem.setIsSelected(mExtrasAdded.containsKey(extra.getId()));
            extraItems.add(extraItem);
        }
        recyclerView.setAdapter(new ExtrasListAdapter(extraItems, new ExtrasListListener() {
            @Override
            public void onExtraClicked(ExtrasListAdapter.ExtraItem extraItem) {
                if (extraItem.isSelected()) {

                }
            }
        }));
    }

    private void updateExtrasListAdapterItems() {
        final RecyclerView recyclerView = mExtrasDialog.findViewById(R.id.extrasList);
        ExtrasListAdapter extrasAdapter = (ExtrasListAdapter) recyclerView.getAdapter();
        List<ExtrasListAdapter.ExtraItem> extraItems = extrasAdapter.getExtras();
        for (ExtrasListAdapter.ExtraItem item : extraItems) {
            Extra extra = item.getExtra();
            item.setIsSelected(mExtrasAdded.containsKey(extra.getId()));
        }
        extrasAdapter.notifyDataSetChanged();
    }

    private void updateQuantityValue() {
        TextView textView = findViewById(R.id.cantidad_field_value);
        textView.setText(Integer.toString(mOrderQuantity));
    }

    private void updateExtrasValue() {
        StringBuilder stringBuilder = new StringBuilder();
        int extrasCount = 0;
        for (Extra extra : mExtrasAdded.values()) {
            if (extrasCount > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(String.format("%s ($%.2f)", extra.getName(), extra.getPrice()));
            extrasCount++;
        }
        TextView extrasTextview = findViewById(R.id.extras_field_value);
        extrasTextview.setText(stringBuilder.toString());
    }

    private void updateClarificationsValue() {
        TextView clarificationsTextView = findViewById(R.id.aclaraciones_field_value);
        clarificationsTextView.setText(mClarifications);
    }

    private void updateOrderCost() {
        Plate plate = getCommerceService().getCommerce(mCommerceId).getPlate(mPlateId);
        double extrasPrice = 0;
        for (Extra extra : mExtrasAdded.values()) {
            extrasPrice += extra.getPrice();
        }
        double pricePerUnit = plate.getPrice() + extrasPrice;
        mOrderPrice = pricePerUnit * mOrderQuantity;
        TextView costTextView = findViewById(R.id.textView_total_cost);
        costTextView.setText(String.format(Locale.ENGLISH, "Total = $%.2f", mOrderPrice));
    }

    private PlateOrder getPlateOrder() {
        return getPurchaseService().getCart().getPlateOrder(mPlateOrderId);
    }

    // Recovering activity state

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(getString(R.string.intent_data_commerce_id), mCommerceId);
        savedInstanceState.putLong(getString(R.string.intent_data_plate_id), mPlateId);
        savedInstanceState.putLong(getString(R.string.intent_data_plate_order_id), mPlateOrderId);
        savedInstanceState.putBoolean(getString(R.string.intent_data_order_plate_modifying), mIsModifyingPlate);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCommerceId = savedInstanceState.getInt(getString(R.string.intent_data_commerce_id));
        mPlateId = savedInstanceState.getLong(getString(R.string.intent_data_plate_id));
        mPlateOrderId = savedInstanceState.getLong(getString(R.string.intent_data_plate_order_id));
        mIsModifyingPlate = savedInstanceState.getBoolean(getString(R.string.intent_data_order_plate_modifying));
    }

    private void obtainCommerceId(Bundle savedInstanceState) {
        if (mCommerceId == -1) {
            Bundle extras = getIntent().getExtras();
            mCommerceId = extras.getInt(getString(R.string.intent_data_commerce_id), -1);
        }
        if ((mCommerceId == -1) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_commerce_id)))) {
            mCommerceId = savedInstanceState.getInt(getString(R.string.intent_data_commerce_id));
        }
    }

    private void obtainPlateId(Bundle savedInstanceState) {
        if (mPlateId == -1L) {
            Bundle extras = getIntent().getExtras();
            mPlateId = extras.getLong(getString(R.string.intent_data_plate_id), -1L);
        }
        if ((mPlateId == -1L) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_plate_id)))) {
            mPlateId = savedInstanceState.getLong(getString(R.string.intent_data_plate_id));
        }
    }

    private void obtainPlateOrderId(Bundle savedInstanceState) {
        if (mPlateOrderId == -1L) {
            Bundle extras = getIntent().getExtras();
            mPlateOrderId = extras.getLong(getString(R.string.intent_data_plate_order_id), -1L);
        }
        if ((mPlateOrderId == -1L) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_plate_order_id)))) {
            mPlateOrderId = savedInstanceState.getLong(getString(R.string.intent_data_plate_order_id));
        }
    }

    private void obtainIsModifyingPlate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(getString(R.string.intent_data_order_plate_modifying))) {
            mIsModifyingPlate = extras.getBoolean(getString(R.string.intent_data_order_plate_modifying));
        } else {
            if ((savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_order_plate_modifying)))) {
                mIsModifyingPlate = savedInstanceState.getBoolean(getString(R.string.intent_data_order_plate_modifying));
            }
        }
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }
}