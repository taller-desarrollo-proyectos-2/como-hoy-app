package com.fiuba.gaff.comohoy;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.MenuItemListAdapter;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.CommerceMenu;
import com.fiuba.gaff.comohoy.model.CommerceMenuItem;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommerceDetailsActivity extends AppCompatActivity {

    private TextView mCommerceTittle;
    private LinearLayout mMenuLayout;

    private int mCommerceId = -1;
    //private double mCommerceLongitud = 0;
    //private double mCommerceLatitud = 0;

    public interface MenuListListener {
        void onPlateClicked(Plate plate);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_details);

        obtainCommerceId(savedInstanceState);
        //obtainCommerceLat(savedInstanceState);
        //obtainCommerceLong(savedInstanceState);

        ImageButton infoB = findViewById(R.id.info);
        infoB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openMoreInfoIntent = new Intent(CommerceDetailsActivity.this, MoreInfoActivity.class);
                openMoreInfoIntent.putExtra(getString(R.string.intent_data_commerce_id), mCommerceId);
                //openMoreInfoIntent.putExtra(getString(R.string.intent_data_commerce_longitud_id), mCommerceLongitud);
                //openMoreInfoIntent.putExtra(getString(R.string.intent_data_commerce_latitud_id), mCommerceLatitud);
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

    /*private void obtainCommerceLat(Bundle savedInstanceState){
        if (mCommerceLatitud == 0) {
            Bundle extras = getIntent().getExtras();
            mCommerceLatitud = extras.getDouble(getString(R.string.intent_data_commerce_latitud_id),0);
        }
        if ((mCommerceLatitud == 0) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_commerce_latitud_id)))) {
            mCommerceLatitud = savedInstanceState.getDouble(getString(R.string.intent_data_commerce_latitud_id));
        }
    }

    private void obtainCommerceLong(Bundle savedInstanceState) {
        if (mCommerceLongitud == 0) {
            Bundle extras = getIntent().getExtras();
            mCommerceLongitud = extras.getDouble(getString(R.string.intent_data_commerce_longitud_id),0);
        }
        if ((mCommerceLongitud == 0) && (savedInstanceState != null) && (savedInstanceState.containsKey(getString(R.string.intent_data_commerce_longitud_id)))) {
            mCommerceLongitud = savedInstanceState.getDouble(getString(R.string.intent_data_commerce_longitud_id));
        }
    }*/

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

    private Commerce getCommerce() {
        return getCommerceService().getCommerce(mCommerceId);
    }

    private void createCommerceMenuView() {
        // TODO obtener el menu a partir de los valores obtenidos para el comercio en la clase
        // BaseCommercesService linea 99
        CommerceMenu menu = new CommerceMenu();
        CommerceMenuItem item1 = new CommerceMenuItem();

        HashMap<String, List<Plate>> platesByCategory = new HashMap<>();
        assignPlatesToCategories(platesByCategory);

        CommerceMenu commerceMenu = createCommerceMenu(platesByCategory);

        List<CommerceMenuItem> menuItems = commerceMenu.getMenuItems();
        for (CommerceMenuItem subMenu : menuItems) {
            View submenuView = createSubMenu(subMenu);
            mMenuLayout.addView(submenuView);
        }
    }

    private void assignPlatesToCategories(HashMap<String, List<Plate>> platesByCategory) {
        List<Plate> commercePlates = getCommerce().getPlates();
        for (Plate plate : commercePlates) {
            List<Category> plateCategories = plate.getCategories();
            for (Category category : plateCategories) {
                // Add each plate to the category it was assigned to if it exists.
                String categoryName = category.getName();
                if (platesByCategory.containsKey(categoryName)) {
                    platesByCategory.get(categoryName).add(plate);
                }
                else {
                    // If map doesnt have the categoty, add it
                    List<Plate> categoryPlates = new ArrayList<Plate>();
                    categoryPlates.add(plate);
                    platesByCategory.put(categoryName, categoryPlates);
                }
            }
        }
    }

    private CommerceMenu createCommerceMenu(HashMap<String, List<Plate>> platesByCategory) {
        CommerceMenu commerceMenu = new CommerceMenu();
        Iterator it = platesByCategory.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<Plate>> pair = (Map.Entry<String, List<Plate>>) it.next();
            String categoryName = pair.getKey();
            List<Plate> plates = pair.getValue();

            CommerceMenuItem menuItem = new CommerceMenuItem(categoryName, plates);
            commerceMenu.addMenuItem(menuItem);
        }
        return commerceMenu;
    }

    private View createSubMenu(CommerceMenuItem submenu) {
        CardView submenuCardView = createCardView();
        ViewGroup parentLayout = createParentLayout();
        View titleView = createSubmenuTitle(submenu);
        View platesView = createPlatesListView(submenu);
        parentLayout.addView(titleView);
        parentLayout.addView(platesView);

        submenuCardView.addView(parentLayout);

        return submenuCardView;
    }

    private CardView createCardView() {
        CardView cardview = new CardView(this);
        cardview.setRadius(10.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cardview.setElevation(2);
            cardview.setCardBackgroundColor(getColor(R.color.gris));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 10, 16, 8);
        cardview.setLayoutParams(params);
        return cardview;
    }

    private ViewGroup createParentLayout() {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        parentLayout.setLayoutParams(lp);

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
        Intent intent = new Intent();
        intent.setClass(CommerceDetailsActivity.this, OrderPlateActivity.class);
        intent.putExtra(getString(R.string.intent_data_commerce_id), mCommerceId);
        intent.putExtra(getString(R.string.intent_data_plate_id), plate.getId());
        startActivity(intent);

        //String message = "clicked " + plate.getName();
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }
}
