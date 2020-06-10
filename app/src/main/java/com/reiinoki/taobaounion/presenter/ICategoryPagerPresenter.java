package com.reiinoki.taobaounion.presenter;

import com.reiinoki.taobaounion.base.IBasePresenter;
import com.reiinoki.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    void getContentByCategoryId(int categoryId);

    void loaderMore(int categoryId);

    void reload(int categoryId);
}
