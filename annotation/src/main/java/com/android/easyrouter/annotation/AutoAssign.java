package com.android.easyrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liuzhao on 2017/10/9.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface AutoAssign {
    String name() default "";
}
