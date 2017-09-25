package com.android.easyrouterdemo;

import android.app.Application;

import com.android.easyrouter.EasyRouter;
import com.android.easyrouter.annotation.DispatcherModules;
import com.android.easyrouter.callback.IRouterCallBack;
import com.android.easyrouter.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/16.
 */
@DispatcherModules({"app","moduleinteract"})
public class EasyRouterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyRouter.init(EasyRouterApp.this).setScheme("easyrouter").setDefaultRouterCallBack(new IRouterCallBack() {
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
                LogUtil.i("default onOpenFailed");
            }
        });

    }
}
