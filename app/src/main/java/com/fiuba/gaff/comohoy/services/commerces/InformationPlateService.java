package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.services.CustomService;
import java.util.List;

public interface InformationPlateService extends CustomService {

    void updateInformationPlateData(Activity activity, UpdateCommercesCallback callback);
    Plate getPlate(int id);
}