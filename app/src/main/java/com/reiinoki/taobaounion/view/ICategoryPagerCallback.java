package com.reiinoki.taobaounion.view;

import com.reiinoki.taobaounion.base.IBaseCallback;
import com.reiinoki.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {

    //get back data
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();

    //for loaderMore
    void onLoaderMoreError();

    void onLoaderMoreEmpty();

    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);
}
