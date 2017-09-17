package com.android.router.dispatcherimpl;

import android.app.Activity;
import android.app.Application;

import com.android.router.dispatcherimpl.model.IntentWraper;
import com.android.router.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/12.
 */

public class EasyRouter {
    public static boolean isInited;
    public static Application mApplication;
    public static EasyRouter mEasyRouter;

    private EasyRouter() {
    }

    public static EasyRouter setScheme(String scheme) {
        if (mEasyRouter == null) {
            synchronized (EasyRouter.class) {
                if (mEasyRouter == null) {
                    mEasyRouter = new EasyRouter();
                    ActivityDispatcher.getActivityDispatcher().setScheme(scheme);
                }
            }
        }
        return mEasyRouter;
    }

    public void init(Application application) {
        if (!isInited) {
            mApplication = application;
            try {
                Class routerInit = Class.forName("com.android.easyrouter.RouterInit");
                if (routerInit != null) {
                    routerInit.getMethod("init").invoke(null);
                    isInited = true;
                }
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }

    public static IntentWraper with(String url) {
        if (!isInited) {
            LogUtil.e("serious error EasyRouter hasn't been inited !!! Before using , You must call EasyRouter.setScheme().init() first");
        }
        return ActivityDispatcher.getActivityDispatcher().withUrl(url);
    }

    public static boolean open(String url) {
        return open(null, url);
    }

    public static boolean open(Activity activity, String url) {
        if (!isInited) {
            LogUtil.e("serious error EasyRouter hasn't been inited !!! Before using , You must call EasyRouter.setScheme().init() first");
            return false;
        }
        return ActivityDispatcher.getActivityDispatcher().open(activity, url);
    }

    public void setDebug(boolean isDebug) {
        LogUtil.setDebug(isDebug);
        LogUtil.i("EasyRouter debug open");
    }

}
