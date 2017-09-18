package com.android.router.dispatcher.idispatcher;

import android.app.Activity;

import java.util.HashMap;

/**
 * Created by liuzhao on 16/12/10.
 */
public interface IActivityInitMap {
    void initActivityMap(HashMap<String, Class<? extends Activity>> activityMap);
}
