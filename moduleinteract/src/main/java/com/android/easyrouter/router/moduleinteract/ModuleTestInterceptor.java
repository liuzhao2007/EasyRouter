package com.android.router.moduleinteract;

import com.android.easyrouter.annotation.Interceptor;

/**
 * Created by liuzhao on 2017/9/20.
 */

@Interceptor
public class ModuleTestInterceptor implements com.android.easyrouter.intercept.IInterceptor {

    @Override
    public boolean intercept() {
        return false;
    }

    @Override
    public void onIntercepted() {

    }
}
