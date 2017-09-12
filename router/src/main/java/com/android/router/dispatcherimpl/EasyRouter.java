package com.android.router.dispatcherimpl;

import android.app.Activity;
import android.app.Application;

import com.android.router.dispatcherimpl.model.IntentWraper;

/**
 * Created by liuzhao on 2017/9/12.
 */

public class EasyRouter {

    public static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
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

}
