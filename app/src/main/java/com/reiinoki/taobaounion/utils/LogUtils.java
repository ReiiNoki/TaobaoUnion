package com.reiinoki.taobaounion.utils;

import android.util.Log;

public class LogUtils {

    private static int currentLev = 4;
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;

    public static void debug(Class clazz, String log) {
        if (currentLev >= DEBUG_LEV) {
            Log.d(clazz.getSimpleName(), log);
        }
    }

    public static void info(Class clazz, String log) {
        if (currentLev >= INFO_LEV) {
            Log.d(clazz.getSimpleName(), log);
        }
    }


    public static void warning(Class clazz, String log) {
        if (currentLev >= WARNING_LEV) {
            Log.d(clazz.getSimpleName(), log);
        }
    }


    public static void error(Class clazz, String log) {
        if (currentLev >= ERROR_LEV) {
            Log.d(clazz.getSimpleName(), log);
        }
    }


}
