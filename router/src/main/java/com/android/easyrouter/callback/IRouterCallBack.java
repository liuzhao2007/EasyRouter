package com.android.easyrouter.callback;

import com.android.easyrouter.dispatcher.dispatcherimpl.model.IntentWrapper;

/**
 * Created by liuzhao on 2017/9/17.
 */

public interface IRouterCallBack {
    /**
     * can get information needen from intentWrapper
     *
     * @param intentWrapper
     */
    void onFound(IntentWrapper intentWrapper);

    void onLost(IntentWrapper intentWrapper);

    void onOpenSuccess(IntentWrapper intentWrapper);

    void onOpenFailed(IntentWrapper intentWrapper, Throwable e);
}
