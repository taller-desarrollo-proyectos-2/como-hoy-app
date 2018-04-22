package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Plate;
import java.util.ArrayList;
import java.util.List;

public class MockInformationPlate implements InformationPlateService {

    private Context mContext;
    private Plate mPlate;

    public MockInformationPlate(Context context){
        mContext = context;
    }

    @Override
    public void updateInformationPlateData(Activity activity, UpdateCommercesCallback callback) {

        String nameC1 = "Pizza de Muzzarella";
        String descriptionC1 = "Salsa de tomate, muzzarella, oregano y aceitunas";
        int priceC1 = 148;

        Plate c1 = new Plate(nameC1,descriptionC1,priceC1);
        c1.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.luigi));

        //callback.onCommercesUpdated();
    }

    @Override
    public Plate getPlate(int id) {
        return mPlate;
    }
}