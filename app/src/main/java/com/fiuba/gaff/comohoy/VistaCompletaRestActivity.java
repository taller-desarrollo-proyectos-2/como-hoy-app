package com.fiuba.gaff.comohoy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class VistaCompletaRestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restourant_completo);

        ImageButton infoB = findViewById(R.id.info);
        infoB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent RestIntent = new Intent(VistaCompletaRestActivity.this, MapsRestaurantActivity.class);
                startActivity(RestIntent);
            }
        });
    }
}
