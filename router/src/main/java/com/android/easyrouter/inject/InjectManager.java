package com.android.easyrouter.inject;

import com.android.easyrouter.util.EasyRouterLogUtils;

/**
 * Created by liuzhao on 2017/10/10.
 */

public class InjectManager {
    private static String injectSuffix = "_AutoAssign";

    public static void inject(Object object) {
        String objectCName = object.getClass().getName();
        String injectCName = objectCName + injectSuffix;
        try {
            Class.forName(injectCName).getMethod("autoAssign", Object.class).invoke(null, object);
        } catch (Exception e) {
            EasyRouterLogUtils.e(e);
        }
    }

}
