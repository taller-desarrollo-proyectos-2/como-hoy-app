package com.fiuba.gaff.comohoy.services;

import com.fiuba.gaff.comohoy.model.Categorie;

import java.util.List;

/**
 * Created by FernandoN on 30/04/2018.
 */

public interface CategorieService extends CustomService {

    void updateFoodData();
    List<Categorie> getCategories();
    Categorie getCategorie(int index);
}
