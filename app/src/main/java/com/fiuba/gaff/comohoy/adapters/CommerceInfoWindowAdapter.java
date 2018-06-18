package com.fiuba.gaff.comohoy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.fragments.CommercesListFragment;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.commerces.MockCommercesService;
import com.fiuba.gaff.comohoy.services.picasso.CircleTransform;
import com.fiuba.gaff.comohoy.services.picasso.PicassoService;
import com.fiuba.gaff.comohoy.utils.RateUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CommerceInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater mInflater;
    private HashMap<Marker, Commerce> mCommercesFromMarkerMap = new HashMap();
    private CommercesListFragment.CommerceListListener mCommerceListListener;

    public CommerceInfoWindowAdapter(LayoutInflater inflater, final HashMap<Marker, Commerce> commercesFromMarkerMap){
        mInflater = inflater;
        mCommercesFromMarkerMap = commercesFromMarkerMap;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        //Carga layout personalizado.
        View v = mInflater.inflate(R.layout.card_view_commerces_list_item, null);
        final Commerce commerce = mCommercesFromMarkerMap.get(marker);

        CardView cardView = v.findViewById(R.id.card_view_comercio_list_item);
        ImageView commercePicture = v.findViewById(R.id.imagenComercio);
        final TextView commerceName = (TextView) v.findViewById(R.id.nombreComercio);
        TextView commerceDescription = (TextView) v.findViewById(R.id.descripcionComercio);
        TextView requestsAmount = (TextView) v.findViewById(R.id.cantPedidosComercio);
        TextView shippingTime = (TextView) v.findViewById(R.id.tiempoEnvioComercio);
        TextView discountsTextView = (TextView) v.findViewById(R.id.descuento_comercio);
        ImageView star1 = v.findViewById(R.id.star1);
        ImageView star2 = v.findViewById(R.id.star2);
        ImageView star3 = v.findViewById(R.id.star3);
        ImageView star4 = v.findViewById(R.id.star4);
        ImageView star5 = v.findViewById(R.id.star5);
        Guideline guideLine = v.findViewById(R.id.left_guideline);

        /*CommercesService commercesService = ServiceLocator.get(CommercesService.class);
        if (commercesService instanceof MockCommercesService) {
            // DEBUG
            Bitmap commercePictureBitmap = commerce.getPicture();
            Drawable commercePictureDrawable = getCommercePictureDrawable(commercePictureBitmap, v.getContext());
            commercePicture.setImageDrawable(commercePictureDrawable);
        } else {
            String uriFormat = "http://34.237.197.99:9000/api/v1/commerces/%d/picture";
            String uri = String.format(uriFormat, commerce.getId());
            Picasso picasso = ServiceLocator.get(PicassoService.class).getPicasso();
            picasso.load(uri).fit().transform(new CircleTransform()).placeholder(R.drawable.progress_animation).error(R.drawable.no_image).into(commercePicture);
        }*/
        commercePicture.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0;
        guideLine.setLayoutParams(params);
        
        commerceName.setText(commerce.getShowableName());

        StringBuilder categoriesStringBuilder = new StringBuilder();
        boolean isFirstWord = true;
        for (Category category : commerce.getCategories()) {
            if (isFirstWord) {
                isFirstWord = false;
            } else {
                categoriesStringBuilder.append(", ");
            }
            categoriesStringBuilder.append(category.getName());
        }
        commerceDescription.setText(categoriesStringBuilder.toString());
        // holder.mRating.setText(String.format("%.1f", commerce.getRating()));
        shippingTime.setText(String.format("%d min", commerce.getLeadTime()));

        if (commerce.getAveragePrice() > 0) {
            requestsAmount.setText(String.format("~$%.0f", commerce.getAveragePrice()));
        } else {
            requestsAmount.setText("-");
        }

        int commerceMaxDiscount = commerce.getMaxDiscount();
        if (commerceMaxDiscount <= 0) {
            discountsTextView.setVisibility(View.GONE);
        }
        else {
            discountsTextView.setVisibility(View.VISIBLE);
            discountsTextView.setText(String.format("Hasta un %%%d de descuento", commerceMaxDiscount));
        }

        double clampedRating = RateUtils.cleanRate(commerce.getRating());
        star1.setImageResource(getStarIdFromValue(clampedRating));
        star2.setImageResource(getStarIdFromValue(clampedRating - 1));
        star3.setImageResource(getStarIdFromValue(clampedRating - 2));
        star4.setImageResource(getStarIdFromValue(clampedRating - 3));
        star5.setImageResource(getStarIdFromValue(clampedRating - 4));

        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

    // Espera un valor entre 0 y 1
    private int getStarIdFromValue(double value) {
        if (value <= 0) {
            return R.drawable.whitestar;
        }
        if (value >= 1) {
            return  R.drawable.yellowstar;
        }
        return R.drawable.halfyellowstar;
    }
    private Drawable getCommercePictureDrawable (Bitmap pictureBitmap, Context context) {
        RoundedBitmapDrawable roundedDrawable = null;
        if (pictureBitmap != null) {
            if (pictureBitmap.getWidth() > pictureBitmap.getHeight()) {
                pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getHeight(), pictureBitmap.getHeight());
            } else if (pictureBitmap.getWidth() < pictureBitmap.getHeight()) {
                pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getWidth(), pictureBitmap.getWidth());
            }
            roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), pictureBitmap);
            roundedDrawable.setCornerRadius(pictureBitmap.getHeight());
        }
        return roundedDrawable;
    }
}
