package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class OrderPlateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plate);

        TextView nombrePlato = (TextView)findViewById(R.id.txtNombreInfoPlato);
        TextView descriPlato = (TextView)findViewById(R.id.txtDescInfoPlato);
        TextView precioPlato = (TextView)findViewById(R.id.txtPrecioInfoPlato);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("Nombre")!= null) { nombrePlato.setText(bundle.getString("Nombre")); }
        if(bundle.getString("Descripcion")!= null) { descriPlato.setText(bundle.getString("Descripcion")); }
        if(bundle.getString("Precio")!= null) { precioPlato.setText(bundle.getString("Precio")); }

        Button agregarPlatoPedido = (Button) findViewById(R.id.agregar_plato_pedido);
        agregarPlatoPedido.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton desplegarOpcionesExtra = (ImageButton) findViewById(R.id.desplegar_opciones);
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
            RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.id_opciones_extra_plato);
            for(int i=0;i<stringList.size();i++){
                RadioButton rb=new RadioButton(OrderPlateActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
                rb.setText(stringList.get(i));
                rg.addView(rb);
            }
            dialog.show();
            }
        });

    }


}