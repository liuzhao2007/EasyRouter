package com.android.easyrouter;

import com.android.router.annotation.Interceptor;
import com.android.router.intercept.IInterceptor;
import com.android.router.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/20.
 */
@Interceptor
public class RouterTestInterceptor implements IInterceptor {

    @Override
    public boolean intercept() {
        LogUtil.i("intercept by me");
        return false;
    }

    @Override
    public void onIntercepted() {

    }
}
