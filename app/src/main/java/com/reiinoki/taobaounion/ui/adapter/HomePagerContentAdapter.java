package com.reiinoki.taobaounion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.model.domain.HomePagerContent;
import com.reiinoki.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {
    List<HomePagerContent.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean dataBean = data.get(position);
        //set data here
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.goods_cover)
        public ImageView cover;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_price)
        public TextView offPriceTv;

        @BindView(R.id.goods_after_off_price)
        public TextView finalPriceTv;

        @BindView(R.id.goods_original_price)
        public TextView originalPriceTv;

        @BindView(R.id.goods_sell_count)
        public TextView sellCountTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(HomePagerContent.DataBean dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());

            Glide.with(context).load(UrlUtils.getCoverPath(dataBean.getPict_url())).into(cover);

            String finalPrice = dataBean.getZk_final_price();
            long couponAmount = dataBean.getCoupon_amount();

            float resultPrice = Float.parseFloat(finalPrice) - couponAmount;

            offPriceTv.setText(String.format(context.getString(R.string.text_goods_off_price), couponAmount));
            finalPriceTv.setText(String.format("%.2f", resultPrice));
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTv.setText(String.format(context.getString(R.string.text_goods_original_price), finalPrice));
            sellCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count), dataBean.getVolume()));
        }
    }
}
