package com.fiuba.gaff.comohoy;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


public class InfoPlateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_plato);

        TextView nombrePlato = (TextView)findViewById(R.id.txtNombreInfoPlato);
        TextView descriPlato = (TextView)findViewById(R.id.txtDescInfoPlato);
        TextView precioPlato = (TextView)findViewById(R.id.txtPrecioInfoPlato);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("Nombre")!= null) { nombrePlato.setText(bundle.getString("Nombre")); }
        if(bundle.getString("Descripcion")!= null) { descriPlato.setText(bundle.getString("Descripcion")); }
        if(bundle.getString("Precio")!= null) { precioPlato.setText(bundle.getString("Precio")); }

    }

}