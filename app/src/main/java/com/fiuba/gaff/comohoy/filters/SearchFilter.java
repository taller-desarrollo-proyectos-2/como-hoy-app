package com.fiuba.gaff.comohoy.filters;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter implements Filter {
    String nombreComercioFiltro;

    public SearchFilter( String nombreFiltro ) {
        nombreComercioFiltro = nombreFiltro.toLowerCase();
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        List<Commerce> filteredList = new ArrayList<>();
        for (int i = 0; i < commerceList.size(); i++) {
            Commerce commerce = commerceList.get(i);
            if (commerce.getName().toLowerCase().contains(nombreComercioFiltro)) {
                filteredList.add(commerce);
            }
        }
        return filteredList;
    }

}