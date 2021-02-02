package com.reiinoki.taobaounion.model.domain;

public interface ILinearItemInfo extends IBaseInfo{

    String getFinalPrice();

    long getCouponAmount();

    long getVolume();
}
