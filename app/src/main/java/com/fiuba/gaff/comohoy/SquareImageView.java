package com.fiuba.gaff.comohoy;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by FernandoN on 09/04/2018.
 */

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // Optimization so we don't measure twice unless we need to
        if (width != height) {
            setMeasuredDimension(width, width);
        }
    }

}