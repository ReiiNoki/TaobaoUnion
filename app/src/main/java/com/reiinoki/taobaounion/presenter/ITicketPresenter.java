package com.reiinoki.taobaounion.presenter;

import com.reiinoki.taobaounion.base.IBasePresenter;
import com.reiinoki.taobaounion.view.ITicketPagerCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {

    void getTicket(String title, String url, String cover);


}
