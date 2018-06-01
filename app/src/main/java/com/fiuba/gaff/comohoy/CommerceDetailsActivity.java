package com.fiuba.gaff.comohoy;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.gaff.comohoy.adapters.MenuItemListAdapter;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.CommerceMenu;
import com.fiuba.gaff.comohoy.model.CommerceMenuItem;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.AddToFavouriteCallback;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.commerces.RemoveFromFavouritesCallback;
import com.fiuba.gaff.comohoy.services.picasso.PicassoService;
import com.fiuba.gaff.comohoy.utils.RateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommerceDetailsActivity extends AppCompatActivity {

    private TextView mCommerceTittle;
    private LinearLayout mMenuLayout;
    private Button mSeeMyOrderButton;
    private ImageView mCommerceLike;

    private int mCommerceId = -1;
    private boolean mUpdatingFavourite = false;
    private boolean mFromFavourites = false;

    //private CarouselView carouselView;
    private ImageView imageViewCommerce;

    public interface MenuListListener {
        void onPlateClicked(Plate plate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_details);

        obtainCommerceId(savedInstanceState);
        obtainFromFavourites(savedInstanceState);

        getPurchaseService().assignCommerce(mCommerceId);

        //carouselView = findViewById(R.id.carouselView);
        imageViewCommerce = findViewById(R.id.imagenRestaurantFondo);

        mCommerceTittle = findViewById(R.id.commerceTittle);
        mMenuLayout = findViewById(R.id.menu_layout);
        mSeeMyOrderButton = findViewById(R.id.button_see_my_order);
        mCommerceLike = findViewById(R.id.commerce_like);

        setUpCarouselView();
        setUpGoToMoreInforButton();
        setupFavouriteButton();
        setupSeeCartButton();
        fillCommercesValues();
        createCommerceMenuView();
        setupRatingStars();
        setUpCommerceStats();
    }

    private void setUpCarouselView() {
        String uriFormat = "http://34.237.197.99:9000/api/v1/commerces/%d/picture";
        String uri = String.format(uriFormat, mCommerceId);
        Picasso picasso = ServiceLocator.get(PicassoService.class).getPicasso();
        picasso.load(uri).fit().centerCrop().placeholder(R.drawable.progress_animation).error(R.drawable.no_image).into(imageViewCommerce);
    }

    private void updateGoToCartButtonVisibility() {
        int visibility = View.GONE;
        if (!getPurchaseService().isCartEmpty()) {
            // TODO animation to call attention?
            visibility = View.VISIBLE;
        }
        mSeeMyOrderButton.setVisibility(visibility);
    }

    private void setUpCommerceStats() {
        Commerce commerce = getCommerce();

        TextView leadTimeTextview = findViewById(R.id.tiempo_envio);
        leadTimeTextview.setText(String.format("%d min. entrega", commerce.getLeadTime()));

        TextView averagePriceTextView = findViewById(R.id.precio_promedio);
        if (commerce.getAveragePrice() > 0) {
            averagePriceTextView.setText(String.format("~$%.0f", commerce.getAveragePrice()));
        } else {
            averagePriceTextView.setText("-");
        }
    }

    private void setupSeeCartButton() {
        mSeeMyOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent seeCartIntent = new Intent(CommerceDetailsActivity.this, CartActivity.class);
                //seeCartIntent.putExtra(getString(R.string.intent_data_commerce_id), mCommerceId);
                startActivity(seeCartIntent);
            }
        });
    }

    private void setUpGoToMoreInforButton() {
        ImageButton infoB = findViewById(R.id.info);
        infoB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openMoreInfoIntent = new Intent(CommerceDetailsActivity.this, MoreInfoActivity.class);
                openMoreInfoIntent.putExtra(getString(R.string.intent_data_commerce_id), mCommerceId);
                startActivity(openMoreInfoIntent);
            }
        });
    }

    private void setupFavouriteButton() {
        final ImageButton favouriteButton = findViewById(R.id.commerce_like);
        final Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showProgressIcon(favouriteButton);
                boolean isFavourite = commerce.isFavourite();

                if(isFavourite && !mUpdatingFavourite){
                    mUpdatingFavourite = true;
                    removeFromFavourites();
                }

                if (!isFavourite && !mUpdatingFavourite){
                    mUpdatingFavourite = true;
                    addToFavourites();
                }
            }
        });
    }

    private void showProgressIcon(ImageButton imageButton) {
        imageButton.setImageDrawable(ContextCompat.getDrawable(CommerceDetailsActivity.this, R.drawable.progress_animation));
    }

    private void addToFavourites() {
        final ImageButton favouriteButton = findViewById(R.id.commerce_like);
        getCommerceService().addToFavourites(this, mCommerceId, new AddToFavouriteCallback() {
            @Override
            public void onSuccess() {
                Commerce commerce = getCommerceService().getCommerce(mCommerceId);
                favouriteButton.setImageDrawable(ContextCompat.getDrawable(CommerceDetailsActivity.this, R.drawable.corazon_rojo));
                mUpdatingFavourite = false;
            }

            @Override
            public void onError(String reason) {
                showToast("No se pudo agregar el comercio a favoritos. Intente más tarde");
                favouriteButton.setImageDrawable(ContextCompat.getDrawable(CommerceDetailsActivity.this, R.drawable.corazon));
                mUpdatingFavourite = false;
            }
        });
    }

    private void removeFromFavourites() {
        final ImageButton favouriteButton = findViewById(R.id.commerce_like);
        getCommerceService().removeFromFavourites(this, mCommerceId, new RemoveFromFavouritesCallback() {
            @Override
            public void onSuccess() {
                Commerce commerce = getCommerceService().getCommerce(mCommerceId);
                favouriteButton.setImageDrawable(ContextCompat.getDrawable(CommerceDetailsActivity.this, R.drawable.corazon));
                mUpdatingFavourite = false;
            }

            @Override
            public void onError(String reason) {
                showToast("No se pudo quitar el comercio de favoritos. Intente más tarde");
                favouriteButton.setImageDrawable(ContextCompat.getDrawable(CommerceDetailsActivity.this, R.drawable.corazon_rojo));
                mUpdatingFavourite = false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGoToCartButtonVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (mFromFavourites) {
                    finish();
                } else {
                    supportFinishAfterTransition();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PurchasesService purchasesService = getPurchaseService();
        purchasesService.clearCart();
        purchasesService.clearPaymentDetails();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(getString(R.string.intent_data_commerce_id), mCommerceId);
        savedInstanceState.putBoolean(getString(R.string.intent_data_fromFavourites), mFromFavourites);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCommerceId = savedInstanceState.getInt(getString(R.string.intent_data_commerce_id));
        mFromFavourites = savedInstanceState.getBoolean(getString(R.string.intent_data_commerce_id), false);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    private void obtainFromFavourites(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        String key = getString(R.string.intent_data_fromFavourites);
        if (extras.containsKey(key)) {
            mFromFavourites = extras.getBoolean(getString(R.string.intent_data_fromFavourites));
        } else {
            if ((savedInstanceState != null) && (savedInstanceState.containsKey(key))) {
                mFromFavourites = savedInstanceState.getBoolean(key);
            }
        }
    }

    private void fillCommercesValues() {
        Commerce commerce = getCommerceService().getCommerce(mCommerceId);
        if (commerce != null) {
            mCommerceTittle.setText(commerce.getShowableName());
            if (commerce.isFavourite()) {
                mCommerceLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.corazon_rojo));
            }
            else{
                mCommerceLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.corazon));
            }
        }
    }

    private void setupRatingStars() {
        ImageView star1 = findViewById(R.id.star1);
        ImageView star2 = findViewById(R.id.star2);
        ImageView star3 = findViewById(R.id.star3);
        ImageView star4 = findViewById(R.id.star4);
        ImageView star5 = findViewById(R.id.star5);

        double clampedRating = RateUtils.cleanRate(getCommerce().getRating());
        star1.setImageResource(getStarIdFromValue(clampedRating));
        star2.setImageResource(getStarIdFromValue(clampedRating - 1));
        star3.setImageResource(getStarIdFromValue(clampedRating - 2));
        star4.setImageResource(getStarIdFromValue(clampedRating - 3));
        star5.setImageResource(getStarIdFromValue(clampedRating - 4));
    }

    private int getStarIdFromValue(double value) {
        if (value <= 0) {
            return R.drawable.whitestar;
        }
        if (value >= 1) {
            return  R.drawable.yellowstar;
        }
        return R.drawable.halfyellowstar;
    }

    private Commerce getCommerce() {
        return getCommerceService().getCommerce(mCommerceId);
    }

    private void createCommerceMenuView() {

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
            cardview.setCardBackgroundColor(getColor(R.color.light_brown));
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
        intent.putExtra(getString(R.string.intent_data_order_plate_modifying), false);
        startActivity(intent);
    }

    private CommercesService getCommerceService() {
        return ServiceLocator.get(CommercesService.class);
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }
}
