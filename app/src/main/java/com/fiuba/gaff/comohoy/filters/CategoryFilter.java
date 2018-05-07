package com.fiuba.gaff.comohoy.filters;


import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilter implements Filter {

    private final String mCategoryName;

    public CategoryFilter(String categoryName) {
        mCategoryName = categoryName;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        List<Commerce> filteredList = new ArrayList<>();
        for (int i = 0; i < commerceList.size(); i++) {
            Commerce commerce = commerceList.get(i);
            List<Category> categories = commerce.getCategories();
            for (Category category : categories) {
                if (category.getName().equals(mCategoryName)) {
                    filteredList.add(commerce);
                }
            }
        }
        return filteredList;
    }
}
