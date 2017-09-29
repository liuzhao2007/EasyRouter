package com.android.easyrouter.config;

import android.app.Application;

import com.android.easyrouter.callback.IRouterCallBack;
import com.android.easyrouter.dispatcher.dispatcherimpl.ActivityDispatcher;
import com.android.easyrouter.util.ClassUtils;
import com.android.easyrouter.util.LogUtil;

import java.util.List;
import java.util.Set;

/**
 * Created by liuzhao on 2017/9/29.
 */

public class EasyRouterConfig {
    private static EasyRouterConfig mEasyRouterConfig;
    public static Application mApplication;
    public static boolean isInited;

    private EasyRouterConfig() {
    }

    public static EasyRouterConfig getInstance() {
        if (mEasyRouterConfig == null) {
            synchronized (EasyRouterConfig.class) {
                if (mEasyRouterConfig == null) {
                    mEasyRouterConfig = new EasyRouterConfig();
                }
            }
        }
        return mEasyRouterConfig;
    }

    public void init(Application application) {
        if (!isInited) {
            mApplication = application;
            try {
                Class routerInit = Class.forName("com.android.easyrouter.RouterInit");
                if (routerInit != null) {
                    routerInit.getMethod("init").invoke(null);
                }
                Set<String> sets = ClassUtils.getFileNameByPackageName(mApplication, "com.android.easyrouter.interceptor");
                for (String string : sets) {
                    LogUtil.i(string);
                    Class moduleInterceptor = Class.forName(string);
                    if (moduleInterceptor != null) {
                        List list = (List) moduleInterceptor.getMethod("initModuleInterceptor").invoke(null);
                        ActivityDispatcher.mInterceptors.addAll(list);
                    }
                }
                isInited = true;
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }

    public EasyRouterConfig setDebug(boolean isDebug) {
        LogUtil.setDebug(isDebug);
        LogUtil.i("EasyRouter debug open");
        return this;
    }

    public EasyRouterConfig setScheme(String scheme) {
        ActivityDispatcher.getActivityDispatcher().setScheme(scheme);
        return this;
    }

    public EasyRouterConfig setDefaultRouterCallBack(IRouterCallBack defaultRouterCallBack) {
        if (defaultRouterCallBack != null) {
            ActivityDispatcher.getActivityDispatcher().setDefaultRouterCallBack(defaultRouterCallBack);
        }
        LogUtil.i("EasyRouter setDefaultRouterCallBack");
        return this;
    }

}
