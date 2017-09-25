package com.android.easyrouterdemo;

import com.android.easyrouter.annotation.Interceptor;
import com.android.easyrouter.intercept.IInterceptor;
import com.android.easyrouter.util.LogUtil;

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
