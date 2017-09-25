package com.android.easyrouter.dispatcher.idispatcher;

import android.app.Activity;

import com.android.easyrouter.callback.IRouterCallBack;


/**
 * Created by liuzhao on 16/12/10.
 */
public interface IActivityDispatcher {

    boolean open(String url);

    boolean open(Activity activity, String url);

    boolean open(Activity activity, String url, IRouterCallBack routerCallBack);

}
