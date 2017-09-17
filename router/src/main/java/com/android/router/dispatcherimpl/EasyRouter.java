package com.android.router.dispatcherimpl;

import android.app.Activity;
import android.app.Application;

import com.android.router.callback.DefaultRouterCallBack;
import com.android.router.callback.RouterCallBack;
import com.android.router.dispatcherimpl.model.IntentWraper;
import com.android.router.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/12.
 */

public class EasyRouter {
    public static boolean isInited;
    public static Application mApplication;
    private static EasyRouter mEasyRouter;

    private EasyRouter() {
    }

    public EasyRouter setScheme(String scheme) {
        ActivityDispatcher.getActivityDispatcher().setScheme(scheme);
        return this;
    }

    public static EasyRouter init(Application application) {
        if (!isInited) {
            mApplication = application;
            try {
                Class routerInit = Class.forName("com.android.easyrouter.RouterInit");
                if (routerInit != null) {
                    routerInit.getMethod("init").invoke(null);
                    isInited = true;
                }
                if (mEasyRouter == null) {
                    synchronized (EasyRouter.class) {
                        if (mEasyRouter == null) {
                            mEasyRouter = new EasyRouter();
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
        return mEasyRouter;
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

    public static boolean open(String url, RouterCallBack routerCallBack) {
        return open(null, url, routerCallBack);
    }

    public static boolean open(Activity activity, String url) {
        return open(activity, url, null);
    }

    public static boolean open(Activity activity, String url, RouterCallBack routerCallBack) {
        if (!isInited) {
            LogUtil.e("serious error EasyRouter hasn't been inited !!! Before using , You must call EasyRouter.setScheme().init() first");
            return false;
        }
        return ActivityDispatcher.getActivityDispatcher().open(activity, url, routerCallBack);
    }

    public EasyRouter setDebug(boolean isDebug) {
        LogUtil.setDebug(isDebug);
        LogUtil.i("EasyRouter debug open");
        return this;
    }

    public EasyRouter setDefaultRouterCallBack(RouterCallBack defaultRouterCallBack) {
        if (defaultRouterCallBack != null) {
            ActivityDispatcher.getActivityDispatcher().setDefaultRouterCallBack(defaultRouterCallBack);
        }
        LogUtil.i("EasyRouter setDefaultRouterCallBack");
        return this;
    }

}
