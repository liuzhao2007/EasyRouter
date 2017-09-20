package com.android.router.moduleinteract;

import com.android.router.annotation.Interceptor;
import com.android.router.intercept.IInterceptor;

/**
 * Created by liuzhao on 2017/9/20.
 */

@Interceptor
public class ModuleTestInterceptor implements IInterceptor {

    @Override
    public boolean intercept() {
        return false;
    }

    @Override
    public void onIntercepted() {

    }
}
