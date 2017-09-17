package com.android.router.dispatcherimpl.model;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;


import com.android.router.callback.RouterCallBack;
import com.android.router.dispatcherimpl.ActivityDispatcher;

import java.io.Serializable;

/**
 * Created by liuzhao on 2017/7/27.
 */

public class IntentWraper {
    public Bundle mBundle;
    public String mUrl;
    public int mInAnimation = -1;
    public int mOutAnimation = -1;
    public int mIntentFlag = -1;
    public int mRequestCode = -1;
    public RouterCallBack mRouterCallBack;

    public IntentWraper withFlags(int flag) {
        this.mIntentFlag = flag;
        return this;
    }

    public IntentWraper(String string) {
        mUrl = string;
        mBundle = new Bundle();
    }

    public void open() {
        ActivityDispatcher.getActivityDispatcher().open(null, this);
    }

    public void open(RouterCallBack routerCallBack) {
        ActivityDispatcher.getActivityDispatcher().open(null, withRouterCallBack(routerCallBack));
    }

    public void open(Activity activity, int requestCode) {
        mRequestCode = requestCode;
        ActivityDispatcher.getActivityDispatcher().open(activity, this);
    }

    public IntentWraper withRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public IntentWraper withRouterCallBack(RouterCallBack routerCallBack) {
        mRouterCallBack = routerCallBack;
        return this;
    }

    public IntentWraper withTransition(int inAnimation, int outAnimation) {
        this.mInAnimation = inAnimation;
        this.mOutAnimation = outAnimation;
        return this;
    }

    public IntentWraper withString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public IntentWraper withInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public IntentWraper withDouble(String key, Double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public IntentWraper withBoolean(String key, Boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public IntentWraper withFloat(String key, Float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public IntentWraper withSerializable(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public IntentWraper withParcelable(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

}
