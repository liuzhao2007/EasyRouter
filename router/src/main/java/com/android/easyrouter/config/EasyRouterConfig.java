package com.android.easyrouter.config;

import android.app.Application;

import com.android.easyrouter.callback.IRouterCallBack;
import com.android.easyrouter.dispatcher.dispatcherimpl.ActivityDispatcher;
import com.android.easyrouter.util.EasyRouterLogUtils;

import java.util.List;

/**
 * Created by liuzhao on 2017/9/29.
 * EasyRouter的配置类
 */

public class EasyRouterConfig {
    private static volatile EasyRouterConfig mEasyRouterConfig;
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
                // deal dispatcher and service
                Class routerInit = Class.forName("com.android.easyrouter.RouterInit");
                if (routerInit != null) {
                    routerInit.getMethod("init").invoke(null);
                }
                for (String string : ActivityDispatcher.sModuleNames) {
                    EasyRouterLogUtils.i(string);
                    // deal interceptor
                    Class moduleInterceptor = Class.forName("com.android.easyrouter.interceptor.AutoCreateModuleInterceptor_" + string);
                    if (moduleInterceptor != null) {
                        List list = (List) moduleInterceptor.getMethod("initModuleInterceptor").invoke(null);
                        ActivityDispatcher.sRealInterceptors.addAll(list);
                    }
                }
                isInited = true;
            } catch (Exception e) {
                EasyRouterLogUtils.e(e);
            }
        }
    }

    public EasyRouterConfig setDebug(boolean isDebug) {
        EasyRouterLogUtils.setDebug(isDebug);
        EasyRouterLogUtils.i("EasyRouter debug open");
        return this;
    }

    public EasyRouterConfig setScheme(String scheme) {
        ActivityDispatcher.getsActivityDispatcher().setScheme(scheme);
        return this;
    }

    public EasyRouterConfig setDefaultRouterCallBack(IRouterCallBack defaultRouterCallBack) {
        if (defaultRouterCallBack != null) {
            ActivityDispatcher.getsActivityDispatcher().setDefaultRouterCallBack(defaultRouterCallBack);
        }
        EasyRouterLogUtils.i("EasyRouter setDefaultRouterCallBack");
        return this;
    }

}
