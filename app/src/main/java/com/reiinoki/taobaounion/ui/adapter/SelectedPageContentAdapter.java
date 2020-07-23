package com.reiinoki.taobaounion.ui.adapter;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.model.domain.SelectedContent;
import com.reiinoki.taobaounion.utils.Constants;
import com.reiinoki.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {

    private List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mData = new ArrayList<>();
    private OnSelectedPageContentItemClickListener mContentItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.debug(this, "SelectedPageContentAdapter InnerHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPageContentAdapter.InnerHolder holder, int position) {
        SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData = mData.get(position);
        holder.setData(itemData);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentItemClickListener != null) {
                    mContentItemClickListener.onContentItemClick(itemData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        LogUtils.debug(this, "right adapter set data - code" + content.getCode());
        if (content.getCode() == Constants.SUCCESS_CODE) {
            List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> uatmTbkItem = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
            this.mData.clear();
            this.mData.addAll(uatmTbkItem);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selected_cover)
        public ImageView cover;

        @BindView(R.id.selected_off_price)
        public TextView offPrizeTv;

        @BindView(R.id.selected_title)
        public TextView title;

        @BindView(R.id.selected_buy_btn)
        public TextView buyBtn;

        @BindView(R.id.selected_original_prize)
        public TextView originalPrizeTv;



        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData) {
            title.setText(itemData.getTitle());
            String pict_url = itemData.getPict_url();
            Glide.with(itemView.getContext()).load(pict_url).into(cover);
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                originalPrizeTv.setText("没有优惠券了！！！");
                buyBtn.setVisibility(View.GONE);
            } else {
                originalPrizeTv.setText("原价：" + itemData.getZk_final_price());
            }

            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPrizeTv.setVisibility(View.GONE);
            } else {
                offPrizeTv.setVisibility(View.VISIBLE);
                offPrizeTv.setText(itemData.getCoupon_info());
            }


        }
    }

    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener) {
        this.mContentItemClickListener = listener;
    }

    public interface OnSelectedPageContentItemClickListener {
        void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item);
    }
}
