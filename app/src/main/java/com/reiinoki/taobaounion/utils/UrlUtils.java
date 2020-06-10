package com.reiinoki.taobaounion.utils;

public class UrlUtils {

    public static String createHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String pict_url) {
        return "https:" + pict_url;
    }
}
