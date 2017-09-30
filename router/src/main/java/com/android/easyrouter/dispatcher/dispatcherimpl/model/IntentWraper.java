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

public class IntentWraper {
    public Intent mIntent;
    public Bundle mBundle;
    public String mUrl;
    public int mInAnimation = -1;
    public int mOutAnimation = -1;
    public int mIntentFlag = -1;
    public int mRequestCode = -1;
    public IRouterCallBack mRouterCallBack;
    public List<IInterceptor> mInterceptors = new ArrayList<>();
    public int openType;// type for url , Activity or Fragment

    public IntentWraper(String string) {
        mUrl = string;
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

    public IntentWraper addInterceptor(IInterceptor interceptor) {
        if (interceptor != null) {
            mInterceptors.add(interceptor);
        }
        return this;
    }

    public IntentWraper withFlags(int flag) {
        this.mIntentFlag = flag;
        return this;
    }

    public IntentWraper withRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public IntentWraper withRouterCallBack(IRouterCallBack routerCallBack) {
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
