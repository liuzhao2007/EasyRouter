package com.android.easyrouter.dispatcher.dispatcherimpl.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;


import com.android.easyrouter.callback.IRouterCallBack;
import com.android.easyrouter.dispatcher.dispatcherimpl.ActivityDispatcher;
import com.android.easyrouter.intercept.IInterceptor;
import com.android.easyrouter.util.EasyRouterConstant;
import com.android.easyrouter.util.EasyRouterLogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhao on 2017/7/27.
 */

public class IntentWrapper {
    public Intent mIntent;
    public Bundle mBundle;
    public String mUrl;
    public String mOriginalUrl;// the variable won't be changed
    public int mInAnimation = -1;
    public int mOutAnimation = -1;
    public int mIntentFlag = -1;
    public int mRequestCode = -1;
    public IRouterCallBack mRouterCallBack;
    public List<IInterceptor> mInterceptors = new ArrayList<>();
    public int openType;// type for url , Activity or Fragment

    public IntentWrapper(String url) {
        mUrl = url;
        mOriginalUrl = url;
        mBundle = new Bundle();
    }

    public boolean open() {
        return open(null);
    }

    public boolean open(IRouterCallBack routerCallBack) {
        return ActivityDispatcher.getActivityDispatcher().open(null, withRouterCallBack(routerCallBack));
    }

    public boolean open(Activity activity, int requestCode) {
        mRequestCode = requestCode;
        return ActivityDispatcher.getActivityDispatcher().open(activity, this);
    }

    public <T> T getFragment(Class<T> tClass) {
        if (tClass == null || !TextUtils.equals(tClass.getSimpleName(), "Fragment")) {
            EasyRouterLogUtils.e(new RuntimeException("getFragment mothod params must be Fragment or support Fragment type"));
            return null;
        }
        openType = EasyRouterConstant.IntentWraperType_Fragment;
        Object object = ActivityDispatcher.getActivityDispatcher().open(this);
        try {
            return (T) object;
        } catch (ClassCastException e) {
            EasyRouterLogUtils.e(e);
            return null;
        }
    }

    public IntentWrapper addInterceptor(IInterceptor interceptor) {
        if (interceptor != null) {
            mInterceptors.add(interceptor);
        }
        return this;
    }

    public IntentWrapper withFlags(int flag) {
        this.mIntentFlag = flag;
        return this;
    }

    public IntentWrapper withRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public IntentWrapper withRouterCallBack(IRouterCallBack routerCallBack) {
        mRouterCallBack = routerCallBack;
        return this;
    }

    public IntentWrapper withTransition(int inAnimation, int outAnimation) {
        this.mInAnimation = inAnimation;
        this.mOutAnimation = outAnimation;
        return this;
    }

    public IntentWrapper withString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public IntentWrapper withInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public IntentWrapper withDouble(String key, Double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public IntentWrapper withBoolean(String key, Boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public IntentWrapper withFloat(String key, Float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public IntentWrapper withSerializable(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public IntentWrapper withParcelable(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

}
