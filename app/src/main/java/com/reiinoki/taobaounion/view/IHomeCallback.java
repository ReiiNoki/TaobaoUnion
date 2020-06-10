package com.reiinoki.taobaounion.view;

import com.reiinoki.taobaounion.base.IBaseCallback;
import com.reiinoki.taobaounion.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {

    void onCategoriesLoaded(Categories categories);

}

