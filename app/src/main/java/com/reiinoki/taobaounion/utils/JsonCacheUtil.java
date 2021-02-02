package com.reiinoki.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.reiinoki.taobaounion.base.BaseApplication;
import com.reiinoki.taobaounion.model.domain.CacheWithDuration;

public class JsonCacheUtil {

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    private JsonCacheUtil() {
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME,Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String valueStr = mGson.toJson(value);
        if (duration != -1L) {
            duration += System.currentTimeMillis();
        }
        //save an object with time and data
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        editor.putString(key, cacheWithTime);
        editor.apply();
    }

    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);

        long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() < 0) {
            //out of date
           return null;
        } else {
            String cache = cacheWithDuration.getCache();
            T result = mGson.fromJson(cache, clazz);
            return result;
        }
    }

    private static JsonCacheUtil sJsonCacheUtil = null;

    public static JsonCacheUtil getInstance() {
        if (sJsonCacheUtil == null) {
            sJsonCacheUtil = new JsonCacheUtil();
        }
        return sJsonCacheUtil;
    }
}
