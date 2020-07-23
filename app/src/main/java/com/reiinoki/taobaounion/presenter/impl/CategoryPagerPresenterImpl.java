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

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;
    private Integer mCurrentPage;

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }

        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.debug(CategoryPagerPresenterImpl.this, "code -> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pageContent = response.body();
                    LogUtils.debug(CategoryPagerPresenterImpl.this, "pageContent -> " + pageContent);
                    //give data to UI
                    handleHomePageContentResult(pageContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.debug(CategoryPagerPresenterImpl.this, "onFailure -> " + t.toString());
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        //load content by category id
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.debug(this, "home pager url -> " + homePagerUrl);
        return api.getHomePagerContent(homePagerUrl);
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
        List<HomePagerContent.DataBean> data = pageContent.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pageContent == null || pageContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(pageContent.getData());
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {
        //1 get current page
        mCurrentPage = pagesInfo.get(categoryId);
        if (pagesInfo == null) {
            mCurrentPage = 1;
        }
        //2 page++
        mCurrentPage++;
        //3 load data
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        //4 deal with data
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                // result
                int code = response.code();
                LogUtils.debug(CategoryPagerPresenterImpl.this, "result code: " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    LogUtils.debug(CategoryPagerPresenterImpl.this, "result: " + result.toString());
                    handleLoaderResult(result, categoryId);
                } else {
                    handleLoaderMoreError(categoryId);
                }

            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.debug(CategoryPagerPresenterImpl.this, t.toString());
                handleLoaderMoreError(categoryId);
            }
        });
    }

    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoaderMoreEmpty();
                } else {
                    callback.onLoaderMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoaderMoreError(int categoryId) {
        mCurrentPage--;
        pagesInfo.put(categoryId,mCurrentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoaderMoreError();
            }
        }
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
