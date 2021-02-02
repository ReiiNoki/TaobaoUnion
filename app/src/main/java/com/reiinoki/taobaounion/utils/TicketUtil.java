package com.reiinoki.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.reiinoki.taobaounion.model.domain.IBaseInfo;
import com.reiinoki.taobaounion.presenter.ITicketPresenter;
import com.reiinoki.taobaounion.ui.activity.TicketActivity;

public class TicketUtil {

    public static void toTicketPage(Context context, IBaseInfo baseInfo) {
        String title = baseInfo.getTitle();
        String url = baseInfo.getUrl();
        if (TextUtils.isEmpty(url)) {
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        //the data loaded by ticketPresenter
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
