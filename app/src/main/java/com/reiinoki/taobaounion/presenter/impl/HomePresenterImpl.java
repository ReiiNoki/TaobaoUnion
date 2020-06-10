package com.reiinoki.taobaounion.presenter.impl;

import com.reiinoki.taobaounion.model.Api;
import com.reiinoki.taobaounion.model.domain.Categories;
import com.reiinoki.taobaounion.presenter.IHomePresenter;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.utils.RetrofitManager;
import com.reiinoki.taobaounion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                // Result of data
                int code = response.code();
                LogUtils.debug(this, "result code is --> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    Categories categories = response.body();
                    if (mCallback != null) {
                        if (categories == null || categories.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            LogUtils.debug(HomePresenterImpl.this, categories.toString());
                            mCallback.onCategoriesLoaded(categories);
                        }
                    }
                } else {
                    LogUtils.info(HomePresenterImpl.this, "request failed");
                    if (mCallback != null) {
                        mCallback.onNetworkError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                // if failed
                LogUtils.error(HomePresenterImpl.this, "request failed" + t);
                if (mCallback != null) {
                    mCallback.onNetworkError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        this.mCallback = null;
    }
}
