package com.reiinoki.taobaounion.ui.adapter;

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
import com.reiinoki.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {

    private List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
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
        SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData = mData.get(position);
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
        LogUtils.debug(this, "right adapter set data - code " + content.getCode());
        if (content.getCode() == Constants.SUCCESS_CODE) {
            try {
                List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> uatmTbkItem = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
                this.mData.clear();
                this.mData.addAll(uatmTbkItem);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        @BindView(R.id.selected_original_price)
        public TextView originalPriceTv;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData) {
            title.setText(itemData.getTitle());
            String pict_url = UrlUtils.getCoverPath(itemData.getPict_url());
            Glide.with(itemView.getContext()).load(pict_url).into(cover);
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                originalPriceTv.setText("没有优惠券了！！！");
                buyBtn.setVisibility(View.GONE);
            } else {
                originalPriceTv.setText("原价：" + itemData.getZk_final_price());
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
        void onContentItemClick(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item);
    }
}
