package com.reiinoki.taobaounion.model;

import com.reiinoki.taobaounion.model.domain.Categories;
import com.reiinoki.taobaounion.model.domain.HomePagerContent;
import com.reiinoki.taobaounion.model.domain.SelectedContent;
import com.reiinoki.taobaounion.model.domain.SelectedPageCategory;
import com.reiinoki.taobaounion.model.domain.TicketParams;
import com.reiinoki.taobaounion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET()
    Call<SelectedContent> getSelectedContent(@Url String url);
}
