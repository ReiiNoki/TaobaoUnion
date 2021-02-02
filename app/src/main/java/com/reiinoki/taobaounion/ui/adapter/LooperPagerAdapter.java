package com.reiinoki.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.reiinoki.taobaounion.model.domain.HomePagerContent;
import com.reiinoki.taobaounion.model.domain.IBaseInfo;
import com.reiinoki.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> mData = new ArrayList<>();
    private OnLooperPageItemClickListener mItemClickListener = null;

    public int getDataSize() {
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        int realPosition = position % mData.size();


        HomePagerContent.DataBean dataBean = mData.get(realPosition);
        int measureHeight = container.getMeasuredHeight();
        int measureWidth = container.getMeasuredWidth();

        int ivSize = Math.max(measureHeight, measureWidth) / 2;
        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(), ivSize);
        ImageView iv = new ImageView(container.getContext());

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(container.getContext()).load(coverUrl).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    HomePagerContent.DataBean item = mData.get(realPosition);
                    mItemClickListener.onLooperItemClick(item);
                }
            }
        });
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void setOnLooperPageItemCLickListener(OnLooperPageItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnLooperPageItemClickListener {
        void onLooperItemClick(IBaseInfo item);
    }
}
