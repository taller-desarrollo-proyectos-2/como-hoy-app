package com.fiuba.gaff.comohoy.services.PurchasesService;

import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.services.CustomService;

public interface PurchasesService extends CustomService {
    void addPlateToCart(PlateOrder palteOrder);

}
