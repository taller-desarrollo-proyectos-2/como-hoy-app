package com.fiuba.gaff.comohoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.model.Commerce;

public class MainActivity extends AppCompatActivity implements CommercesListFragment.CommerceListListener {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*View include_restaurant = findViewById(R.id.id_restaurant_prueba);
        include_restaurant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent RestIntent = new Intent(MainActivity.this, VistaCompletaRestActivity.class);
                startActivity(RestIntent);
            }
        });*/

    }

    @Override
    public void onCommerceClicked(Commerce commerce) {
        Intent intent = new Intent();
        intent.setClass(this, VistaCompletaRestActivity.class);
        //intent.putExtra("index", commerceIndex);
        startActivity(intent);
    }
}
