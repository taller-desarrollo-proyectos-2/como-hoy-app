package com.fiuba.gaff.comohoy.filters;

import com.fiuba.gaff.comohoy.model.Commerce;
import java.util.List;

public interface Filter {
    List<Commerce> apply(List<Commerce> commerceList);
}
