package com.reiinoki.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseFragment;
import com.reiinoki.taobaounion.model.domain.SelectedContent;
import com.reiinoki.taobaounion.model.domain.SelectedPageCategory;
import com.reiinoki.taobaounion.presenter.ISelectedPagePresenter;
import com.reiinoki.taobaounion.presenter.ITicketPresenter;
import com.reiinoki.taobaounion.ui.activity.TicketActivity;
import com.reiinoki.taobaounion.ui.adapter.SelectedPageContentAdapter;
import com.reiinoki.taobaounion.ui.adapter.SelectedPageLeftAdapter;
import com.reiinoki.taobaounion.utils.PresenterManager;
import com.reiinoki.taobaounion.utils.SizeUtils;
import com.reiinoki.taobaounion.view.ISelectedPageCallback;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemListener, SelectedPageContentAdapter.OnSelectedPageContentItemClickListener {

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;

    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mRightAdapter;

    private ISelectedPagePresenter mSelectedPagePresenter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
    }

    @Override
    protected void release() {
        super.release();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_selected;
    }

    protected void initView(View rootView) {
        setupState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        //set
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(),4);
                int leftAndRight = SizeUtils.dip2px(getContext(), 6);
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom;
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnSelectedPageContentItemClickListener(this);
    }

    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setupState(State.SUCCESS);
        mLeftAdapter.setData(categories);
        //data from getCategories()
//        LogUtils.debug(this, "onCategoriesLoaded: " + categories);
//        List<SelectedPageCategory.DataBean> data = categories.getData();
//        mSelectedPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);
    }

    @Override
    protected void onRetryClick() {
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reloadContent();
        }
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        mSelectedPagePresenter.getContentByCategory(item);
    }

    @Override
    public void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item) {
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
}
