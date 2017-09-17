package com.android.router.callback;

import com.android.router.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/17.
 */

public class DefaultRouterCallBack implements RouterCallBack {
    @Override
    public void onFound() {
        LogUtil.i("onFound please cheak for reason");
    }

    @Override
    public void onLost() {
        LogUtil.e("onLost please cheak for reason");
    }

    @Override
    public void onOpenSuccess() {
        LogUtil.i("onOpenSuccess");
    }

    @Override
    public void onOpenFailed() {
        LogUtil.e("onOpenFailed please cheak for reason");
    }
}
