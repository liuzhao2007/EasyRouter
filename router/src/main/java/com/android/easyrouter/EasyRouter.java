package com.android.easyrouter;

import android.app.Activity;

import com.android.easyrouter.callback.IRouterCallBack;
import com.android.easyrouter.config.EasyRouterConfig;
import com.android.easyrouter.dispatcher.dispatcherimpl.ActivityDispatcher;
import com.android.easyrouter.dispatcher.dispatcherimpl.model.IntentWraper;
import com.android.easyrouter.service.IBaseModuleService;
import com.android.easyrouter.service.ModuleServiceManager;
import com.android.easyrouter.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/12.
 */

public class EasyRouter {
    private static EasyRouter mEasyRouter;

    private EasyRouter() {
    }

    // api for UI
    public static boolean open(String url) {
        return open(null, url);
    }

    public static boolean open(String url, IRouterCallBack routerCallBack) {
        return open(null, url, routerCallBack);
    }

    public static boolean open(Activity activity, String url) {
        return open(activity, url, null);
    }

    public static boolean open(Activity activity, String url, IRouterCallBack routerCallBack) {
        if (!EasyRouterConfig.isInited) {
            LogUtil.e("serious error EasyRouter hasn't been inited !!! Before using , You must call EasyRouter.setScheme().init() first");
            return false;
        }
        return ActivityDispatcher.getActivityDispatcher().open(activity, url, routerCallBack);
    }

    public static IntentWraper with(String url) {
        if (!EasyRouterConfig.isInited) {
            LogUtil.e("serious error EasyRouter hasn't been inited !!! Before using , You must call EasyRouter.setScheme().init() first");
        }
        return ActivityDispatcher.getActivityDispatcher().withUrl(url);
    }

    // api for service
    public static <T extends IBaseModuleService> T getModuleService(Class<T> tClass) {
        return ModuleServiceManager.getModuleService(tClass);
    }

}
