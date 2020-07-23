package com.reiinoki.taobaounion.view;

import com.reiinoki.taobaounion.base.IBaseCallback;
import com.reiinoki.taobaounion.model.domain.SelectedContent;
import com.reiinoki.taobaounion.model.domain.SelectedPageCategory;

public interface ISelectedPageCallback extends IBaseCallback {

    void onCategoriesLoaded(SelectedPageCategory categories);

    void onContentLoaded(SelectedContent content);
}
