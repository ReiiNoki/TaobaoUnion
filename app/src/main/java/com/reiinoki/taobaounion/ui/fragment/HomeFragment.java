package com.reiinoki.taobaounion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseFragment;
import com.reiinoki.taobaounion.model.domain.Categories;
import com.reiinoki.taobaounion.presenter.IHomePresenter;
import com.reiinoki.taobaounion.presenter.impl.HomePresenterImpl;
import com.reiinoki.taobaounion.ui.adapter.HomePagerAdapter;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.view.IHomeCallback;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    private IHomePresenter mHomePresenter;

    @BindView(R.id.home_pager)
    public ViewPager homePager;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(homePager);
        //setup adapter for ViewPager
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        homePager.setAdapter(mHomePagerAdapter);
    }


    @Override
    protected void initPresenter() {
        //create presenter
        mHomePresenter = new HomePresenterImpl();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }

    @Override
    protected void loadData() {
        //load data
        setupState(State.LOADING);
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        if (categories == null || categories.getData().size() == 0) {
            setupState(State.EMPTY);
        } else {
            setupState(State.SUCCESS);
        }
        LogUtils.debug(this, "onCategoriesLoaded... ");
        //get back the loaded data
        if (mHomePagerAdapter !=null){
            mHomePagerAdapter.setCategories(categories);
        }

    }

    @Override
    protected void release() {
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
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
    protected void onRetryClick() {
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }
}
