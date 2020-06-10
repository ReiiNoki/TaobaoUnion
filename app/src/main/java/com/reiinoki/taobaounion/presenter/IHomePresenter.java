package com.reiinoki.taobaounion.presenter;

import com.reiinoki.taobaounion.base.IBasePresenter;
import com.reiinoki.taobaounion.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    // get categories of goods
    void getCategories();



}
