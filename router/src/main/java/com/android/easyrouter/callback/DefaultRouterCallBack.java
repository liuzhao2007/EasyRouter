package com.android.easyrouter.callback;


import com.android.easyrouter.util.EasyRouterLogUtils;

/**
 * Created by liuzhao on 2017/9/17.
 */

public class DefaultRouterCallBack implements IRouterCallBack {
    @Override
    public void onFound() {
        EasyRouterLogUtils.i("onFound");
    }

    @Override
    public void onLost() {
        EasyRouterLogUtils.e("onLost please cheak for reason");
    }

    @Override
    public void onOpenSuccess() {
        EasyRouterLogUtils.i("onOpenSuccess");
    }

    @Override
    public void onOpenFailed() {
        EasyRouterLogUtils.e("onOpenFailed please cheak for reason");
    }
}
