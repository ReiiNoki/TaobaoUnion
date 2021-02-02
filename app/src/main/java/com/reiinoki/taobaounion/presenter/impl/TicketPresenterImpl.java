package com.reiinoki.taobaounion.presenter.impl;

import com.reiinoki.taobaounion.model.Api;
import com.reiinoki.taobaounion.model.domain.TicketParams;
import com.reiinoki.taobaounion.model.domain.TicketResult;
import com.reiinoki.taobaounion.presenter.ITicketPresenter;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.utils.RetrofitManager;
import com.reiinoki.taobaounion.utils.UrlUtils;
import com.reiinoki.taobaounion.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketPagerCallback mViewCallback = null;
    private String mCover = null;
    private TicketResult mTicketResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        this.onLoadingTicket();
        this.mCover = cover;
        String targetUrl = UrlUtils.getTicketUrl(url);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(targetUrl, title);
        api.getTicket(ticketParams);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.debug(TicketPresenterImpl.this, "result code: " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    mTicketResult = response.body();
                    LogUtils.debug(TicketPresenterImpl.this, "result: " + mTicketResult);
                    //inform UI to update
                    mCurrentState = LoadState.SUCCESS;
                    onLoadedTicketSuccess();
                } else {
                    onLoadedTicketError();
                    mCurrentState = LoadState.ERROR;
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onLoadedTicketError();
                mCurrentState = LoadState.ERROR;
            }
        });
    }

    private void onLoadedTicketSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        } else {
            mCurrentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = callback;
        if (mCurrentState != LoadState.NONE) {
            if (mCurrentState == LoadState.SUCCESS) {
                onLoadedTicketSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (mCurrentState == LoadState.LOADING) {
                onLoadingTicket();
            }
        }

    }

    private void onLoadingTicket() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = null;
    }
}
