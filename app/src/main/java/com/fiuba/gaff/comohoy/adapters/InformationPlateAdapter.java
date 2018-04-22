package com.fiuba.gaff.comohoy.adapters;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.InformationPlateFragment;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.commerces.InformationPlateService;

import java.util.List;


public class InformationPlateAdapter extends RecyclerView.Adapter<InformationPlateAdapter.InformationPlateViewHolder> {

    private final InformationPlateFragment.InformationPlateListener mInformationPlatetListener;
    private Plate mPlate;

    public InformationPlateAdapter(Plate plate, InformationPlateFragment.InformationPlateListener listener){
        mPlate = plate;
        mInformationPlatetListener = listener;
    }

    public static class InformationPlateViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
       // private final ImageView mPicture;
        private final TextView mName;
        private final TextView mDescription;
        private final TextView mPrice;

        InformationPlateViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
         //   mPicture = (ImageView) itemView.findViewById(R.id.imagenComercio);
            mName = (TextView) itemView.findViewById(R.id.txtNombreInfoPlato);
            mDescription = (TextView) itemView.findViewById(R.id.txtDescInfoPlato);
            mPrice = (TextView) itemView.findViewById(R.id.txtPrecioInfoPlato);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public InformationPlateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_plato, parent, false);
        InformationPlateViewHolder informationPlateViewHolder = new InformationPlateViewHolder(v);
        return informationPlateViewHolder;
    }

    @Override
    public void onBindViewHolder(final InformationPlateViewHolder holder, int position) {
        final Plate plate = mPlate;

     //   holder.mPicture.setImageBitmap(plate.getPicture());
        holder.mName.setText(plate.getName());
        holder.mDescription.setText(plate.getDescription());
        holder.mPrice.setText(plate.getPrice());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInformationPlatetListener.onInformationPlateClicked(plate, holder.mName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
