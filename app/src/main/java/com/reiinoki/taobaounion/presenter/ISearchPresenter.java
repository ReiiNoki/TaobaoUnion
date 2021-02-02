package com.reiinoki.taobaounion.presenter;

import com.reiinoki.taobaounion.base.IBasePresenter;
import com.reiinoki.taobaounion.view.ISearchPageCallBack;

public interface ISearchPresenter extends IBasePresenter<ISearchPageCallBack> {

    void getHistories();

    void delHistories();

    void doSearch(String keyword);

    void research();

    void loaderMore();

    void getRecommendWords();


}