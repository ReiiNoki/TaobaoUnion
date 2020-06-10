package com.reiinoki.taobaounion.ui.fragment;

import android.view.View;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseFragment;

public class SearchFragment extends BaseFragment {
    @Override
    protected int getRootViewId() {
        return R.layout.fragment_search;
    }

    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }
}
