package com.android.easyrouter.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by liuzhao on 2017/9/30.
 */

public class EasyRouterUtils {

    public static void runOnUiThread(Runnable runnable) {
        if (runnable == null) {
            EasyRouterLogUtils.e("runOnUiThread is called but runnable is null");
            return;
        }
        new Handler(Looper.getMainLooper()).post(runnable);
    }

}
