package com.reiinoki.taobaounion.utils;

import com.reiinoki.taobaounion.presenter.ICategoryPagerPresenter;
import com.reiinoki.taobaounion.presenter.IHomePresenter;
import com.reiinoki.taobaounion.presenter.ISelectedPagePresenter;
import com.reiinoki.taobaounion.presenter.ITicketPresenter;
import com.reiinoki.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.reiinoki.taobaounion.presenter.impl.HomePresenterImpl;
import com.reiinoki.taobaounion.presenter.impl.SelectedPagePresenterImpl;
import com.reiinoki.taobaounion.presenter.impl.TicketPresenterImpl;

public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final ISelectedPagePresenter mSelectedPagePresenter;

    public static PresenterManager getInstance() {
        return ourInstance;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CategoryPagerPresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
    }

    public ICategoryPagerPresenter getCategoryPagePresenter() {
        return mCategoryPagePresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public ISelectedPagePresenter getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }
}
