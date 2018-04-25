package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.ArrayList;
import java.util.List;


public class OrderPlateActivity extends AppCompatActivity {

    private int mCommerceId = -1;
    private Long mPlateId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plate2);

        obtainCommerceId(savedInstanceState);
        obtainPlateId(savedInstanceState);

        fillFieldsWithPlateData();

        Button agregarPlatoPedido = findViewById(R.id.add_plate_button);
        agregarPlatoPedido.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

       /* ImageButton desplegarOpcionesExtra = (ImageButton) findViewById(R.id.desplegar_opciones);
        desplegarOpcionesExtra.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            final Dialog dialog = new Dialog(OrderPlateActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.opciones_extra_plato);
            List<String> stringList=new ArrayList<>();  // here is list
            for(int i=0;i<5;i++) {
                stringList.add("RadioButton " + (i + 1));
            }
            RadioGroup rg = dialog.findViewById(R.id.id_opciones_extra_plato);
            for(int i=0;i<stringList.size();i++){
                RadioButton rb=new RadioButton(OrderPlateActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
                rb.setText(stringList.get(i));
                rg.addView(rb);
            }
            dialog.show();
            }
        });*/

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
        platePriceTextField.setText(String.format("$%.1f", plate.getPrice()));
        if (!plate.isSuitableForCeliac()) {
            celiacImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(getString(R.string.intent_data_commerce_id), mCommerceId);
        savedInstanceState.putLong(getString(R.string.intent_data_plate_id), mPlateId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCommerceId = savedInstanceState.getInt(getString(R.string.intent_data_commerce_id));
        mPlateId = savedInstanceState.getLong(getString(R.string.intent_data_plate_id));
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
        if (mPlateId == -1) {
            Bundle extras = getIntent().getExtras();
            mPlateId = extras.getLong(getString(R.string.intent_data_plate_id), -1);
        }
        if ((mPlateId == -1) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_plate_id)))) {
            mPlateId = savedInstanceState.getLong(getString(R.string.intent_data_plate_id));
        }
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }


}