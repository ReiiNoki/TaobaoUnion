package com.reiinoki.taobaounion.base;

import com.reiinoki.taobaounion.view.IHomeCallback;

public interface IBasePresenter<T> {

    void registerViewCallback(T callback);

    void unregisterViewCallback(T callback);
}
