package com.fiuba.gaff.comohoy.filters;

import com.fiuba.gaff.comohoy.model.Commerce;
import java.util.List;

public class DistanceFilter implements Filter {
    List<Commerce> mCommerceList;

    public DistanceFilter(List<Commerce> commerceList) {
        mCommerceList = commerceList;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        return mCommerceList;
    }
}