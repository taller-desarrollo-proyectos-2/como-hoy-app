package com.fiuba.gaff.comohoy.services.commerces;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.CustomService;

import java.util.List;

public interface CommercesService extends CustomService {

    void syncFromServer();
    List<Commerce> getCommerces();
    Commerce getCommerce(int index);
}
