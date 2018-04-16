package com.fiuba.gaff.comohoy;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.MenuItemListAdapter;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.CommerceMenu;
import com.fiuba.gaff.comohoy.model.CommerceMenuItem;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.List;

public class CommerceDetailsActivity extends AppCompatActivity {

    private TextView mCommerceTittle;
    private LinearLayout mMenuLayout;

    private int mCommerceId = -1;

    public interface MenuListListener {
        void onPlateClicked(Plate plate);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_details);

        obtainCommerceId(savedInstanceState);

        ImageButton infoB = findViewById(R.id.info);
        infoB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openMoreInfoIntent = new Intent(CommerceDetailsActivity.this, MapsRestaurantActivity.class);
                openMoreInfoIntent.putExtra(getString(R.string.intent_data_commerce_id), mCommerceId);
                startActivity(openMoreInfoIntent);
            }
        });

        mCommerceTittle = findViewById(R.id.commerceTittle);
        mMenuLayout = findViewById(R.id.menu_layout);

        fillCommercesValues();
        createCommerceMenuView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(getString(R.string.intent_data_commerce_id), mCommerceId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCommerceId = savedInstanceState.getInt(getString(R.string.intent_data_commerce_id));
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

    private void fillCommercesValues() {
        Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        if (commerce != null) {
            mCommerceTittle.setText(commerce.getShowableName());

        }
    }

    private void createCommerceMenuView() {
        // TODO obtener el menu a partir de los valores obtenidos para el comercio en la clase
        // BaseCommercesService linea 99
        CommerceMenu menu = new CommerceMenu();
        CommerceMenuItem item1 = new CommerceMenuItem();
        item1.setCategory("Categoria 1");
        item1.addPlate(new Plate("plato 1", "moluscos y otras cosas", 50));
        item1.addPlate(new Plate("plato 2", "moluscos y otras cosas", 150));
        item1.addPlate(new Plate("plato 3", "moluscos y otras cosas", 550));
        CommerceMenuItem item2 = new CommerceMenuItem();
        item2.setCategory("Categoria 2");
        item2.addPlate(new Plate("plato 1", "moluscos y otras cosas", 50));
        item2.addPlate(new Plate("plato 2", "moluscos y otras cosas", 150));
        item2.addPlate(new Plate("plato 3", "moluscos y otras cosas", 550));
        menu.addMenuItem(item1);
        menu.addMenuItem(item2);

        List<CommerceMenuItem> menuItems = menu.getMenuItems();
        for (CommerceMenuItem subMenu : menu.getMenuItems()) {
            View submenuView = createSubMenu(subMenu);
            mMenuLayout.addView(submenuView);
        }
    }

    private View createSubMenu(CommerceMenuItem submenu) {
        ViewGroup parentLayout = createParentLayout();
        View titleView = createSubmenuTitle(submenu);
        View platesView = createPlatesListView(submenu);
        parentLayout.addView(titleView);
        parentLayout.addView(platesView);

        return parentLayout;
    }

    private ViewGroup createParentLayout() {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(16, 8, 8, 16);
        parentLayout.setLayoutParams(lp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            parentLayout.setBackgroundColor(getColor(R.color.gris));
        }

        return parentLayout;
    }

    private View createSubmenuTitle(CommerceMenuItem submenu) {
        TextView titleView = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(16, 8, 8, 16);
        titleView.setLayoutParams(lp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleView.setTextAppearance(android.R.style.TextAppearance_Material_Medium);
        }
        titleView.setText(submenu.getCategory());
        return titleView;
    }

    private View createPlatesListView(CommerceMenuItem submenu) {
        RecyclerView platesList = new RecyclerView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        platesList.setLayoutParams(lp);
        platesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        platesList.setAdapter(new MenuItemListAdapter(submenu.getPlates(), new MenuListListener() {
            @Override
            public void onPlateClicked(Plate plate) {
                showPlateClicked(plate);
            }
        }));
        return platesList;
    }

    private void showPlateClicked(Plate plate) {
        String message = "clicked " + plate.getName();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }
}
