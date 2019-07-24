package com.tck.common;

import android.util.Log;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 13:31</p>
 *
 * @author tck
 * @version 1.0
 */
public class MyLog {

    private static final String TAG = "tck6666";

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}
