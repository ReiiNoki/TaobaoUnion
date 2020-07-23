package com.reiinoki.taobaounion.presenter.impl;

import com.reiinoki.taobaounion.model.Api;
import com.reiinoki.taobaounion.model.domain.SelectedContent;
import com.reiinoki.taobaounion.model.domain.SelectedPageCategory;
import com.reiinoki.taobaounion.presenter.ISelectedPagePresenter;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.utils.RetrofitManager;
import com.reiinoki.taobaounion.utils.UrlUtils;
import com.reiinoki.taobaounion.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectedPagePresenter {

    private final Api mApi;
    private ISelectedPageCallback mViewCallback = null;

    public SelectedPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int resultCode = response.code();
                LogUtils.debug(SelectedPagePresenterImpl.this, "result code: " + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedPageCategory result = response.body();
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {
        String targetUrl = UrlUtils.getSelectedPageContentUrl(item.getFavorites_id());
        Call<SelectedContent> task = mApi.getSelectedContent(targetUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                int resultCode = response.code();
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedContent result = response.body();
                    if (mViewCallback != null) {
                        mViewCallback.onContentLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reloadContent() {
        this.getCategories();
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = null;
    }
}
