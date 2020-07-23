package com.reiinoki.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseFragment;
import com.reiinoki.taobaounion.model.domain.Categories;
import com.reiinoki.taobaounion.model.domain.HomePagerContent;
import com.reiinoki.taobaounion.presenter.ICategoryPagerPresenter;
import com.reiinoki.taobaounion.presenter.ITicketPresenter;
import com.reiinoki.taobaounion.presenter.impl.TicketPresenterImpl;
import com.reiinoki.taobaounion.ui.activity.TicketActivity;
import com.reiinoki.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.reiinoki.taobaounion.ui.adapter.LooperPagerAdapter;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;
import com.reiinoki.taobaounion.ui.custom.AutoLoopViewPager;
import com.reiinoki.taobaounion.utils.Constants;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.utils.PresenterManager;
import com.reiinoki.taobaounion.utils.SizeUtils;
import com.reiinoki.taobaounion.utils.ToastUtil;
import com.reiinoki.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePagerContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private ICategoryPagerPresenter mCategoryPagePresenter;
    private int mMaterialId;
    private HomePagerContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

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

    @BindView(R.id.loop_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointerContainer;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homePagerHeaderContainer;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.debug(this, "onResume");
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
    }

    @Override
    protected void initListener() {
        mContentAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPageItemCLickListener(this);
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homePagerHeaderContainer == null) {
                    return;
                }
                int headerHeight = homePagerHeaderContainer.getMeasuredHeight();
                homePagerNestedView.setHeaderHeight(headerHeight);
                int measureHeight = homePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measureHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measureHeight != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });


        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                //switch indicator
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
               if (mCategoryPagePresenter != null) {
                    mCategoryPagePresenter.loaderMore(mMaterialId);
               }
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }
        });
    }

    //switch indicator
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointerContainer.getChildCount(); i++) {
            View point = looperPointerContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            }else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }

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
        //pager adapter
        mLooperPagerAdapter = new LooperPagerAdapter();
        looperPager.setAdapter(mLooperPagerAdapter);
        //set refresh attributes
        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
       mCategoryPagePresenter = PresenterManager.getInstance().getCategoryPagePresenter();
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

        if (currentCategoryTitleTv != null) {
            currentCategoryTitleTv.setText(title);
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
    public void onError() {

    }

    @Override
    public void onLoaderMoreError() {
        ToastUtil.showToast("network error, please try later");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtil.showToast("no more goods");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //add to the bottom of adapter
        mContentAdapter.addData(contents);
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast(contents.size() + "records loaded");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        mLooperPagerAdapter.setData(contents);

        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPositon = (Integer.MAX_VALUE / 2) - dx;

        looperPager.setCurrentItem(targetCenterPositon);
        looperPointerContainer.removeAllViews();

        //add point in looper point container
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            }else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointerContainer.addView(point);
        }
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

    @Override
    public void onItemClick(HomePagerContent.DataBean item) {
        LogUtils.debug(this, "item onclick");
        handleItemCLick(item);
    }

    private void handleItemCLick(HomePagerContent.DataBean item) {
        String title = item.getTitle();
        String url = item.getCoupon_click_url();
        if (TextUtils.isEmpty(url)) {
            url = item.getClick_url();
        }
        String cover = item.getPict_url();
        //oad data using TicketPresenter
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    @Override
    public void onLooperItemClick(HomePagerContent.DataBean item) {
        handleItemCLick(item);
    }
}

