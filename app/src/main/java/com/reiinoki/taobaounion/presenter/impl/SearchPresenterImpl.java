package com.reiinoki.taobaounion.presenter.impl;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.reiinoki.taobaounion.model.Api;
import com.reiinoki.taobaounion.model.domain.Histories;
import com.reiinoki.taobaounion.model.domain.SearchRecommend;
import com.reiinoki.taobaounion.model.domain.SearchResult;
import com.reiinoki.taobaounion.presenter.ISearchPresenter;
import com.reiinoki.taobaounion.utils.JsonCacheUtil;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.utils.RetrofitManager;
import com.reiinoki.taobaounion.view.ISearchPageCallBack;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {

    private final Api mApi;
    private ISearchPageCallBack mSearchPageCallBack = null;
    private String mCurrentKeyword = null;
    private final JsonCacheUtil mJsonCacheUtil;

    public SearchPresenterImpl() {
        RetrofitManager instance = RetrofitManager.getInstance();
        Retrofit retrofit = instance.getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    public static final int DEFAULT_PAGE = 0;

    private int mCurrentPage = DEFAULT_PAGE;

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchPageCallBack != null) {
            mSearchPageCallBack.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
        if (mSearchPageCallBack != null) {
            mSearchPageCallBack.onHistoriesDeleted();
        }
    }

    public static final String KEY_HISTORIES = "key_histories";

    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private int mHistoriesMaxSize = DEFAULT_HISTORIES_SIZE;

    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        //if the histories exist, clear and set a new one
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //there is no data
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        //limit size
        if (historiesList.size() > mHistoriesMaxSize) {
            historiesList = historiesList.subList(0, mHistoriesMaxSize);
        }
        //add record in histories
        historiesList.add(history);
        //save record
        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)) {
            this.saveHistory(keyword);
            this.mCurrentKeyword = keyword;
        }

        if (mSearchPageCallBack != null) {
            mSearchPageCallBack.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.debug(SearchPresenterImpl.this, "on Response -> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });
    }

    private void handleSearchResult(SearchResult result) {
        if (mSearchPageCallBack != null) {
            if (isResultEmpty(result)) {
                mSearchPageCallBack.onEmpty();
            } else {
                mSearchPageCallBack.onSearchSuccess(result);
            }
        }
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void onError() {
        if (mSearchPageCallBack != null) {
            mSearchPageCallBack.onError();
        }
    }

    @Override
    public void research() {
        if (mCurrentKeyword == null) {
            if (mSearchPageCallBack != null) {
                mSearchPageCallBack.onEmpty();
            }
        } else {
            this.doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;
        if (mCurrentKeyword == null) {
            if (mSearchPageCallBack != null) {
                mSearchPageCallBack.onEmpty();
            }
        } else {
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderMoreError();
            }
        });
    }

    private void handleMoreSearchResult(SearchResult result) {
        if (mSearchPageCallBack != null) {
            if (isResultEmpty(result)) {
                mSearchPageCallBack.onMoreLoadedEmpty();
            } else {
                mSearchPageCallBack.onMoreLoaded(result);
            }
        }
    }

    private void onLoaderMoreError() {
        mCurrentPage--;
        if (mSearchPageCallBack != null) {
            mSearchPageCallBack.onMoreLoadedError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.debug(SearchPresenterImpl.this, "getRecommendWords result code: " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    if (mSearchPageCallBack != null) {
                        mSearchPageCallBack.onRecommendWordsLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallBack callback) {
        this.mSearchPageCallBack = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallBack callback) {
        this.mSearchPageCallBack = null;
    }
}
