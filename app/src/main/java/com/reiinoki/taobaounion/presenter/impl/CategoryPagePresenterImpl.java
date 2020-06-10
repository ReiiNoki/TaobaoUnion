package com.reiinoki.taobaounion.presenter.impl;

import com.reiinoki.taobaounion.model.Api;
import com.reiinoki.taobaounion.model.domain.HomePagerContent;
import com.reiinoki.taobaounion.presenter.ICategoryPagerPresenter;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.utils.RetrofitManager;
import com.reiinoki.taobaounion.utils.UrlUtils;
import com.reiinoki.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;

    private CategoryPagePresenterImpl() {

    }

    private static ICategoryPagerPresenter sInstance = null;

    public static ICategoryPagerPresenter getsInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagePresenterImpl();
        }
        return sInstance;
    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        //load content by category id
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.debug(this, "home pager url -> " + homePagerUrl);
        Call<HomePagerContent> task = api.getHomePagerContent(homePagerUrl);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.debug(CategoryPagePresenterImpl.this, "code -> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pageContent = response.body();
                    LogUtils.debug(CategoryPagePresenterImpl.this, "pageContent -> " + pageContent);
                    //give data to UI
                    handleHomePageContentResult(pageContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.debug(CategoryPagePresenterImpl.this, "onFailure -> " + t.toString());
            }
        });
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onNetworkError();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pageContent, int categoryId) {
        //UI update data
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pageContent == null || pageContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    callback.onContentLoaded(pageContent.getData());
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }


}
