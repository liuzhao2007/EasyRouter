package com.android.router.idispatcher;

import android.app.Activity;

import com.android.router.callback.RouterCallBack;

/**
 * Created by liuzhao on 16/12/10.
 */
public interface IActivityDispatcher {

    boolean open(String url);

    boolean open(Activity activity, String url);

    boolean open(Activity activity, String url, RouterCallBack routerCallBack);

}
