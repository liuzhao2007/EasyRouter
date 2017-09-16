package com.android.router.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by liuzhao on 2017/7/29.
 */
@Retention(RetentionPolicy.CLASS)
public @interface DispatcherModules {
    String[] value();
}
