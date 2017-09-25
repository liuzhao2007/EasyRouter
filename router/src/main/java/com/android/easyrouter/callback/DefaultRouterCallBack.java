package com.android.easyrouter.callback;


import com.android.easyrouter.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/17.
 */

public class DefaultRouterCallBack implements IRouterCallBack {
    @Override
    public void onFound() {
        LogUtil.i("onFound");
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
