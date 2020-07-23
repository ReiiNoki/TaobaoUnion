package com.reiinoki.taobaounion.presenter;

import com.reiinoki.taobaounion.base.IBasePresenter;
import com.reiinoki.taobaounion.view.IOnSellPageCallBack;

public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallBack> {
    /**
     * load content of on sell page
     */
    void getOnSellContent();

    /**
     * reload content
     *
     * @Call network error, load after network recover
     */
    void reLoad();

    /**
     * load more contents from on sell page
     */
    void loaderMore();
}
