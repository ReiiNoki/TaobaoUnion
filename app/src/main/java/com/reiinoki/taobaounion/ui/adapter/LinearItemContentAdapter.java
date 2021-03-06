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
import com.reiinoki.taobaounion.model.domain.IBaseInfo;
import com.reiinoki.taobaounion.model.domain.ILinearItemInfo;
import com.reiinoki.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {
    List<ILinearItemInfo> mData = new ArrayList<>();
    private OnListItemClickListener mItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ILinearItemInfo dataBean = mData.get(position);
        //set data here
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        //get old list size
        int olderSize = mData.size();
        mData.addAll(contents);
        notifyItemRangeChanged(olderSize, contents.size());
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

        public void setData(ILinearItemInfo dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());

//            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
//            int width = layoutParams.width;
//            int height = layoutParams.height;
//            int coverSize = Math.max(width, height) / 2;

            String coverPath = UrlUtils.getCoverPath(dataBean.getCover());
            Glide.with(context).load(coverPath).into(cover);

            String finalPrice = dataBean.getFinalPrice();
            long couponAmount = dataBean.getCouponAmount();

            float resultPrice = Float.parseFloat(finalPrice) - couponAmount;

            offPriceTv.setText(String.format(context.getString(R.string.text_goods_off_price), couponAmount));
            finalPriceTv.setText(String.format("%.2f", resultPrice));
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTv.setText(String.format(context.getString(R.string.text_goods_original_price), finalPrice));
            sellCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count), dataBean.getVolume()));
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnListItemClickListener{
        void onItemClick(IBaseInfo item);
    }
}
