package com.android.router.intercept;

/**
 * Created by liuzhao on 2017/9/18.
 */

public interface IInterceptor {

    boolean intercept();

    void onIntercepted();
}
