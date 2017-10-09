package com.android.easyrouterdemo;

import android.app.Application;

import com.android.easyrouter.config.EasyRouterConfig;
import com.android.easyrouter.annotation.DispatcherModules;
import com.android.easyrouter.callback.IRouterCallBack;
import com.android.easyrouter.dispatcher.dispatcherimpl.model.IntentWrapper;
import com.android.easyrouter.util.EasyRouterLogUtils;

/**
 * Created by liuzhao on 2017/9/16.
 */
@DispatcherModules({"app", "moduleinteract"})
public class EasyRouterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyRouterConfig.getInstance()
                .setScheme("easyrouter")
                .setDebug(true)
                .setDefaultRouterCallBack(new IRouterCallBack() {
                    @Override
                    public void onFound(IntentWrapper intentWrapper) {
                        EasyRouterLogUtils.i("default onFound");
                    }

                    @Override
                    public void onLost(IntentWrapper intentWrapper) {
                        EasyRouterLogUtils.i("default onLost");
                    }

                    @Override
                    public void onOpenSuccess(IntentWrapper intentWrapper) {
                        EasyRouterLogUtils.i("default onOpenSuccess");
                    }

                    @Override
                    public void onOpenFailed(IntentWrapper intentWrapper, Throwable e) {
                        EasyRouterLogUtils.i("default onOpenFailed");
                    }
                })
                .init(EasyRouterApp.this);

    }

}
