package com.reiinoki.taobaounion.view;

import com.reiinoki.taobaounion.base.IBaseCallback;
import com.reiinoki.taobaounion.model.domain.TicketResult;

public interface ITicketPagerCallback extends IBaseCallback {

    void onTicketLoaded(String cover, TicketResult result);
}
