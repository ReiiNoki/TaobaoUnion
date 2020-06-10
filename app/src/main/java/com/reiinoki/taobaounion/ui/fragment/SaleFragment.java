package com.reiinoki.taobaounion.ui.fragment;

import android.view.View;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseFragment;

public class SaleFragment extends BaseFragment {
    @Override
    protected int getRootViewId() {
        return R.layout.fragment_sale;
    }

    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }
}
