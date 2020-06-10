package com.reiinoki.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseFragment;
import com.reiinoki.taobaounion.model.domain.Categories;
import com.reiinoki.taobaounion.model.domain.HomePagerContent;
import com.reiinoki.taobaounion.presenter.ICategoryPagerPresenter;
import com.reiinoki.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.reiinoki.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.reiinoki.taobaounion.utils.Constants;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter mCategoryPagePresenter;
    private int mMaterialId;
    private HomePagerContentAdapter mContentAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        //setup layout manager
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //start adapter
        mContentAdapter = new HomePagerContentAdapter();
        //setup adapter
        mContentList.setAdapter(mContentAdapter);
    }

    @Override
    protected void initPresenter() {
       mCategoryPagePresenter = CategoryPagePresenterImpl.getsInstance();
       mCategoryPagePresenter.registerViewCallback(this);
    }


    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        LogUtils.debug(this, "title ->" + title);
        LogUtils.debug(this, "ID -> " + mMaterialId);
        //load data
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.getContentByCategoryId(mMaterialId);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //data list loaded
        mContentAdapter.setData(contents);
        setupState(State.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {

    }

    @Override
    public void onLoaderMoreEmpty() {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    protected void release() {
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }
}

