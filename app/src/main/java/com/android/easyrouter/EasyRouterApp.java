package com.android.easyrouter;

import android.app.Application;

import com.android.router.annotation.DispatcherModules;
import com.android.router.callback.RouterCallBack;
import com.android.router.dispatcherimpl.EasyRouter;
import com.android.router.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/16.
 */
@DispatcherModules("app")
public class EasyRouterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyRouter.init(EasyRouterApp.this).setScheme("easyrouter").setDefaultRouterCallBack(new RouterCallBack() {
            @Override
            public void onFound() {
                LogUtil.i("default onFound");
            }

            @Override
            public void onLost() {
                LogUtil.i("default onLost");
            }

            @Override
            public void onOpenSuccess() {
                LogUtil.i("default onOpenSuccess");
            }

            @Override
            public void onOpenFailed() {

            }
        });

    }
}
