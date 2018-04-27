package com.fiuba.gaff.comohoy.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuba.gaff.comohoy.OrderPlateActivity;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Extra;

import java.util.List;
import java.util.Locale;

public class ExtrasListAdapter extends RecyclerView.Adapter<ExtrasListAdapter.ExtraItemViewHolder> {

    private final OrderPlateActivity.ExtrasListListener mExtrasListListener;
    private List<ExtraItem> mExtras;

     public ExtrasListAdapter(List<ExtraItem> extraItems, OrderPlateActivity.ExtrasListListener listener){
        mExtras = extraItems;
        mExtrasListListener = listener;
     }

     public List<ExtraItem> getExtras(){
         return mExtras;
    }

     public static class ExtraItem {
         private final Extra mExtra;
         private boolean mIsSelected;

         public ExtraItem(Extra extra) {
             mExtra = extra;
             mIsSelected = false;
        }

        public Extra getExtra() {
             return mExtra;
        }

        public void setIsSelected(boolean value) {
             mIsSelected = value;
        }

        public boolean isSelected() {
             return mIsSelected;
        }
    }

    public static class ExtraItemViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final View mExtraCheckBox;
        private final TextView mExtraText;

        ExtraItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCardView = itemView.findViewById(R.id.cardview_extra);
            mExtraCheckBox = itemView.findViewById(R.id.extra_check);
            mExtraText = itemView.findViewById(R.id.extra_text);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ExtrasListAdapter.ExtraItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_extra, parent, false);
        ExtrasListAdapter.ExtraItemViewHolder extraItemViewHolder = new ExtrasListAdapter.ExtraItemViewHolder(v);
        return extraItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ExtrasListAdapter.ExtraItemViewHolder holder, int position) {
        final ExtraItem extraItem = mExtras.get(position);
        Extra extra = extraItem.getExtra();
        holder.mExtraCheckBox.setVisibility(getCheckboxVisibility(extraItem.isSelected()));
        holder.mExtraText.setText(String.format(Locale.ENGLISH, "%s (+%.2f)", extra.getName(), extra.getPrice()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraItem.setIsSelected(!extraItem.isSelected());
                holder.mExtraCheckBox.setVisibility(getCheckboxVisibility(extraItem.isSelected()));
                mExtrasListListener.onExtraClicked(extraItem);
            }
        });

    }

    private int getCheckboxVisibility(boolean isSelected) {
        if (isSelected) {
            return View.VISIBLE;
        }
        return View.INVISIBLE;
    }

    @Override
    public int getItemCount() {
        return mExtras.size();
    }
}
