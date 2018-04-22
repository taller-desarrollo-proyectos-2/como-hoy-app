package com.fiuba.gaff.comohoy.filters;

import com.fiuba.gaff.comohoy.model.Commerce;
import java.util.List;

public class PriceFilter implements Filter {
    List<Commerce> mCommerceList;

    public PriceFilter( List<Commerce> commerceList) {
        mCommerceList = commerceList;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        return mCommerceList;
    }
}
