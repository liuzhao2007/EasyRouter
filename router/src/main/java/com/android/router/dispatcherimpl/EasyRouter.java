package com.android.router.dispatcherimpl;

import android.app.Activity;
import android.app.Application;

import com.android.router.dispatcherimpl.model.IntentWraper;
import com.android.router.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/12.
 */

public class EasyRouter {

    public static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
        try {
            Class routerInit = Class.forName("com.android.easyrouter.RouterInit");
            if (routerInit != null) {
                routerInit.getMethod("init").invoke(null);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public static IntentWraper with(String url) {
        return ActivityDispatcher.getActivityDispatcher().withUrl(url);
    }

    public static boolean open(String url) {
        return open(null, url);
    }

    public static boolean open(Activity activity, String url) {
        return ActivityDispatcher.getActivityDispatcher().open(activity, url);
    }

    public static void setDebug(boolean isDebug) {
        LogUtil.setDebug(isDebug);
        LogUtil.i("EasyRouter debug open");
    }

}
