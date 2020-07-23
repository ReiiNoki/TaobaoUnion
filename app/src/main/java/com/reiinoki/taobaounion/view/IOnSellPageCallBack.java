package com.reiinoki.taobaounion.view;

import com.reiinoki.taobaounion.base.IBaseCallback;
import com.reiinoki.taobaounion.model.domain.OnSellContent;

public interface IOnSellPageCallBack extends IBaseCallback {

    void onContentLoadedSuccess(OnSellContent result);

    void onMoreLoaded(OnSellContent moreResult);

    void onMoreLoadedError();

    void omMoreLoadedEmpty();
}
