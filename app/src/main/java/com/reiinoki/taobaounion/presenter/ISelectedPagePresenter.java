package com.reiinoki.taobaounion.presenter;

import com.reiinoki.taobaounion.base.IBasePresenter;
import com.reiinoki.taobaounion.model.domain.SelectedPageCategory;
import com.reiinoki.taobaounion.view.ISelectedPageCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {

    void getCategories();

    void getContentByCategory(SelectedPageCategory.DataBean item);

    void reloadContent();

}
