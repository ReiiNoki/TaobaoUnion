package com.reiinoki.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseActivity;
import com.reiinoki.taobaounion.model.domain.TicketResult;
import com.reiinoki.taobaounion.presenter.ITicketPresenter;
import com.reiinoki.taobaounion.ui.custom.LoadingView;
import com.reiinoki.taobaounion.utils.LogUtils;
import com.reiinoki.taobaounion.utils.PresenterManager;
import com.reiinoki.taobaounion.utils.ToastUtil;
import com.reiinoki.taobaounion.utils.UrlUtils;
import com.reiinoki.taobaounion.view.ITicketPagerCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private ITicketPresenter mTicketPresenter;

    private boolean mHasTaobaoApp;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_button)
    public TextView mOpenOrCopyBtn;

    @BindView(R.id.ticket_back_press)
    public View backPress;

    @BindView(R.id.ticket_cover_loading)
    public LoadingView loadingView;

    @BindView(R.id.ticket_load_retry)
    public View retryLoadingText;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        mTicketPresenter.registerViewCallback(this);

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobaoApp = (packageInfo != null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobaoApp = false;
        }
        mOpenOrCopyBtn.setText(mHasTaobaoApp ? "打开淘宝领券" : "复制淘口令");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticketCode = mTicketCode.getText().toString().trim();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clipData = ClipData.newPlainText("taobao_ticket_code", ticketCode);
                clipboardManager.setPrimaryClip(clipData);

                if (mHasTaobaoApp) {
                    Intent taobaoIntent = new Intent();
//                    taobaoIntent.setAction("android.intent.action.MAIN");
//                    taobaoIntent.addCategory("android.intent.category.LAUNCHER");
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                }else {
                    ToastUtil.showToast("已经复制，粘贴分享或打开淘宝");
                }
            }
        });
    }

    @Override
    public void onError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadingText != null) {
            retryLoadingText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {

        LogUtils.debug(this, "onTicketLoaded");
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String coverPath = UrlUtils.getCoverPath(cover);
            Glide.with(this).load(coverPath).into(mCover);
        }

        if (TextUtils.isEmpty(cover)) {
            mCover.setImageResource(R.mipmap.ic_launcher);
        }

        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            mTicketCode.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
        }

        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
            LogUtils.debug(this, "loading view gone");
        }
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onLoading() {
        if (retryLoadingText != null) {
            retryLoadingText.setVisibility(View.GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
