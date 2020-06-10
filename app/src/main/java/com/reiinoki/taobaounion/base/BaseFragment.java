package com.reiinoki.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private Unbinder mBind;
    private FrameLayout mBaseContainer;

    @OnClick(R.id.network_error_tips)
    public void retry() {
        LogUtils.debug(this, "on retry...");
        onRetryClick();
    }

    protected void  onRetryClick() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStateView(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initPresenter();
        loadData();
        return rootView;
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);

        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);

        //error page
        mErrorView = loadErrorView(inflater,container);
        mBaseContainer.addView(mErrorView);

        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        setupState(State.NONE);
    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container,false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    public void setupState(State state) {
        this.currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    //Loading page
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected void initView(View rootView) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
        release();

    }

    protected void release() {

    }

    protected void initPresenter() {

    }

    protected void loadData() {

    };

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resID = getRootViewId();
        return inflater.inflate(resID, container, false);
    };

    protected abstract int getRootViewId();
}
