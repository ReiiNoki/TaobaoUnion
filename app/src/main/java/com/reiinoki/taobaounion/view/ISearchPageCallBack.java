package com.reiinoki.taobaounion.view;

import com.reiinoki.taobaounion.base.IBaseCallback;
import com.reiinoki.taobaounion.model.domain.Histories;
import com.reiinoki.taobaounion.model.domain.SearchRecommend;
import com.reiinoki.taobaounion.model.domain.SearchResult;

import java.util.List;

public interface ISearchPageCallBack extends IBaseCallback {

    void onHistoriesLoaded(Histories histories);

    void onHistoriesDeleted();

    void onSearchSuccess(SearchResult result);

    void onMoreLoaded(SearchResult result);

    void onMoreLoadedError();

    void onMoreLoadedEmpty();

    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
