package com.reiinoki.taobaounion.presenter.impl;

import com.reiinoki.taobaounion.model.Api;
import com.reiinoki.taobaounion.model.domain.OnSellContent;
import com.reiinoki.taobaounion.presenter.IOnSellPagePresenter;
import com.reiinoki.taobaounion.utils.RetrofitManager;
import com.reiinoki.taobaounion.utils.UrlUtils;
import com.reiinoki.taobaounion.view.IOnSellPageCallBack;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterImpl implements IOnSellPagePresenter {

    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellPageCallBack mOnSellPageCallBack;
    private final Api mApi;

    public OnSellPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //inform UI situation as loading
        if (mOnSellPageCallBack != null) {
            mOnSellPageCallBack.onLoading();
        }
        //get on sell content

        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });
    }

    private void onSuccess(OnSellContent result) {
        if (mOnSellPageCallBack != null) {
            try {
                if (!isEmpty(result)) {
                    onEmpty();
                } else {
                    mOnSellPageCallBack.onContentLoadedSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mOnSellPageCallBack.onContentLoadedSuccess(result);
        }
    }

    private boolean isEmpty(OnSellContent content) {
        int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size == 0;
    }

    private void onEmpty() {
        if (mOnSellPageCallBack != null) {
            mOnSellPageCallBack.onEmpty();
        }
    }

    private void onError() {
        mIsLoading = false;
        if (mOnSellPageCallBack != null) {
            mOnSellPageCallBack.onError();
        }
    }

    @Override
    public void reLoad() {
        this.getOnSellContent();
    }

    //recent situation
    private boolean mIsLoading = false;

    @Override
    public void loaderMore() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        mCurrentPage++;
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onMoreLoaded(result);
                } else {
                    onMoreLoadedError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onMoreLoadedError();
            }
        });

    }

    private void onMoreLoaded(OnSellContent result) {
        if (mOnSellPageCallBack != null) {
            if (!isEmpty(result)) {
                mOnSellPageCallBack.onMoreLoaded(result);
            } else {
                mCurrentPage--;
                mOnSellPageCallBack.omMoreLoadedEmpty();
            }
        }
    }

    private void onMoreLoadedError() {
        mIsLoading = false;
        mCurrentPage--;
        mOnSellPageCallBack.onMoreLoadedError();
    }

    @Override
    public void registerViewCallback(IOnSellPageCallBack callback) {
        this.mOnSellPageCallBack = callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallBack callback) {
        this.mOnSellPageCallBack = null;
    }
}
