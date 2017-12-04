package com.android.easyrouter.intercept;

import com.android.easyrouter.dispatcher.dispatcherimpl.model.IntentWrapper;

/**
 * Created by liuzhao on 2017/9/18.
 */

public interface IInterceptor {

    boolean intercept(IntentWrapper intentWrapper);

    void onIntercepted(IntentWrapper intentWrapper);
}
