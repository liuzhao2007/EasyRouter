package com.android.easyrouter;

import android.app.Application;

import com.android.router.annotation.DispatcherModules;
import com.android.router.dispatcherimpl.EasyRouter;

/**
 * Created by liuzhao on 2017/9/16.
 */
@DispatcherModules("app")
public class EasyRouterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyRouter.init(EasyRouterApp.this);

    }
}
