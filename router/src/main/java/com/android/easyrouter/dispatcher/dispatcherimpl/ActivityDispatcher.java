package com.android.easyrouter.dispatcher.dispatcherimpl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.android.easyrouter.callback.DefaultRouterCallBack;
import com.android.easyrouter.callback.IRouterCallBack;
import com.android.easyrouter.config.EasyRouterConfig;
import com.android.easyrouter.dispatcher.dispatcherimpl.model.DisPatcherInfo;
import com.android.easyrouter.dispatcher.dispatcherimpl.model.IntentWrapper;
import com.android.easyrouter.dispatcher.idispatcher.IActivityDispatcher;
import com.android.easyrouter.dispatcher.idispatcher.IActivityInitMap;
import com.android.easyrouter.intercept.IInterceptor;
import com.android.easyrouter.util.EasyRouterConstant;
import com.android.easyrouter.util.EasyRouterLogUtils;
import com.android.easyrouter.util.EasyRouterUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by liuzhao on 16/10/21.
 */
public class ActivityDispatcher implements IActivityDispatcher {
    private static volatile ActivityDispatcher sActivityDispatcher;
    public static String SCHEME = "easyrouter";
    private int DEFAULTVALUE = -1;
    public static List<String> sModuleNames = new ArrayList<String>();// store module names
    public HashMap<String, Class> mRealActivityMaps = new HashMap<String, Class>();// store the mapping of strings and class
    private static IRouterCallBack sDefaultRouterCallBack;
    public static List<Object> sRealInterceptors = new ArrayList<Object>();// store the interceptor for UI Action
    private static final String CHARSET = "UTF-8";//code character set

    private ActivityDispatcher() {
    }

    public static ActivityDispatcher getsActivityDispatcher() {
        if (sActivityDispatcher == null) {
            synchronized (ActivityDispatcher.class) {
                if (sActivityDispatcher == null) {
                    sActivityDispatcher = new ActivityDispatcher();
                    sDefaultRouterCallBack = new DefaultRouterCallBack();
                }
            }
        }
        return sActivityDispatcher;
    }

    public void setDefaultRouterCallBack(IRouterCallBack defaultRouterCallBack) {
        sDefaultRouterCallBack = defaultRouterCallBack;
    }

    public void initActivityMaps(IActivityInitMap activityInitMap) {
        activityInitMap.initActivityMap(mRealActivityMaps);
        String name = activityInitMap.getClass().getSimpleName();
        if (!TextUtils.isEmpty(name) && name.contains("_")) {
            String splits[] = name.split("_");
            if (splits != null && splits.length > 1 && !sModuleNames.contains(splits[1])) {
                sModuleNames.add(splits[1]);
            }
        }
    }

    public void initInterceptors(List<IInterceptor> interceptors) {
        sRealInterceptors.addAll(interceptors);
    }

    public IntentWrapper withUrl(String string) {
        return new IntentWrapper(string);
    }

    @Override
    public boolean open(String url) {
        return open(null, url, null);
    }

    @Override
    public boolean open(Activity activity, String url) {
        return open(activity, url, null);
    }

    @Override
    public boolean open(Activity activity, String url, IRouterCallBack routerCallBack) {
        return realOpen(activity, new IntentWrapper(url).withRouterCallBack(routerCallBack)) != null;
    }

    public boolean open(Activity activity, IntentWrapper intentWrapper) {
        return realOpen(activity, intentWrapper) != null ? true : false;
    }

    public Object open(IntentWrapper intentWrapper) {
        return realOpen(null, intentWrapper);
    }

    private Object realOpen(final Activity activity, final IntentWrapper intentWrapper) {
        Object object = null;
        IRouterCallBack routerCallBack = sDefaultRouterCallBack;
        try {
            if (TextUtils.isEmpty(intentWrapper.mUrl) || !canOpen(intentWrapper.mUrl)) {
                throw new RuntimeException("EasyRouter's url mustn't be null");
            }
            if (!canOpen(intentWrapper.mUrl)) {
                throw new RuntimeException("EasyRouter'url doesn't match the given host");
            }

            // deal callBack
            if (intentWrapper.mRouterCallBack != null) {
                routerCallBack = intentWrapper.mRouterCallBack;
            }

            // may optimize
            List<IInterceptor> interceptors = new ArrayList<IInterceptor>();
            for (Object mObject : sRealInterceptors) {
                if (mObject != null) {
                    interceptors.add((IInterceptor) mObject);
                }
            }

            if (intentWrapper.mInterceptors != null && !intentWrapper.mInterceptors.isEmpty()) {
                interceptors.addAll(intentWrapper.mInterceptors);
            }

            for (IInterceptor interceptor : interceptors) {
                if (interceptor != null && interceptor.intercept(intentWrapper)) {
                    interceptor.onIntercepted(intentWrapper);
                    throw new RuntimeException("Original url is intercepted in EasyRouter");
                }
            }

            // pass the original url to the destination
            intentWrapper.withString(EasyRouterConstant.ORIGINALURL, intentWrapper.mOriginalUrl);

            intentWrapper.mUrl = encodeUrl(intentWrapper.mUrl);
            DisPatcherInfo disPatcherInfo = getTargetClass(intentWrapper.mUrl);
            if (disPatcherInfo == null) {
                routerCallBack.onLost(intentWrapper);
                return null;
            }
            routerCallBack.onFound(intentWrapper);

            if (intentWrapper.openType != EasyRouterConstant.IntentWraperType_Fragment) {
                // for Activity
                Intent intent = new Intent(activity == null ? EasyRouterConfig.mApplication : activity, disPatcherInfo.targetClass);
                intent = setParams(intent, intentWrapper.mUrl, disPatcherInfo.matchUrl);
                intent.putExtras(intentWrapper.mBundle);
                if (intentWrapper.mIntentFlag != DEFAULTVALUE) {
                    intent.addFlags(intentWrapper.mIntentFlag);
                }
                intentWrapper.mIntent = intent;
                if (activity == null) {
                    intentWrapper.mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // had better force to run in main thread
                    EasyRouterUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EasyRouterConfig.mApplication.startActivity(intentWrapper.mIntent);
                        }
                    });
                } else {
                    // had better force to run in main thread
                    EasyRouterUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.startActivityForResult(intentWrapper.mIntent, intentWrapper.mRequestCode);
                            if (intentWrapper.mInAnimation != DEFAULTVALUE && intentWrapper.mOutAnimation != DEFAULTVALUE) {
                                activity.overridePendingTransition(intentWrapper.mInAnimation, intentWrapper.mOutAnimation);
                            }
                        }
                    });
                }
                object = intentWrapper;
            } else {
                // for Fragment
                Class fragmentClass = disPatcherInfo.targetClass;
                Object fragmentInstance = fragmentClass.getConstructor().newInstance();
                if (fragmentInstance instanceof Fragment) {
                    ((Fragment) fragmentInstance).setArguments(intentWrapper.mBundle);
                } else if (fragmentInstance instanceof android.support.v4.app.Fragment) {
                    ((android.support.v4.app.Fragment) fragmentInstance).setArguments(intentWrapper.mBundle);
                }
                object = fragmentInstance;
            }
            routerCallBack.onOpenSuccess(intentWrapper);
            return object;
        } catch (Exception e) {
            routerCallBack.onOpenFailed(intentWrapper, e);
            EasyRouterLogUtils.e(e);
            return null;
        }
    }

    private Intent setParams(Intent intent, String targetUrl, String matchUrl) {
        Uri targetUri = Uri.parse(targetUrl);
        List<String> targetSegments = targetUri.getPathSegments();
        Uri matchUri = Uri.parse(matchUrl);
        List<String> segments = matchUri.getPathSegments();

        for (String string : segments) {
            if (string.contains(":")) {
                String paramsType = "";
                String paramsName = "";
                //说明是参数序列；
                if (string.startsWith(":")) {
                    paramsType = "s";
                    paramsName = string.substring(1, string.length());
                } else {
                    paramsType = string.substring(0, 1);//params type；
                    paramsName = string.substring(2, string.length());//params name；
                }
                switch (paramsType) {
                    case "i":
                        //mean int type；
                        try {
                            int intParamValue = Integer.parseInt(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, intParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, -100);
                        }
                        break;
                    case "f":
                        //mean float type；
                        try {
                            float floatParamValue = Float.parseFloat(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, floatParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, 0.0f);
                        }
                        break;
                    case "b":
                        //mean boolean type；
                        try {
                            boolean booleanParamValue = Boolean.parseBoolean(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, booleanParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, false);
                        }
                        break;
                    case "d":
                        //mean double type；
                        try {
                            double doubleParamValue = Double.parseDouble(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, doubleParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, 0.0d);
                        }
                        break;
                    case "s":
                        //mean string type；
                        intent.putExtra(paramsName, targetSegments.get(segments.indexOf(string)));
                        break;
                }
            }
        }
        Set<String> queryParameterNames = targetUri.getQueryParameterNames();
        for (String queryParameterName : queryParameterNames) {
            intent.putExtra(queryParameterName, targetUri.getQueryParameter(queryParameterName));
        }
        return intent;
    }

    public String getScheme() {
        return SCHEME;
    }

    public void setScheme(String scheme) {
        this.SCHEME = scheme;
    }

    /**
     * 对Url进行编码；
     *
     * @param url
     * @return
     */
    private static String encodeUrl(String url) {
        String realUrl = url;
        try {
            realUrl = URLDecoder.decode(url, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realUrl;
    }

    public boolean canOpen(String url) {
        return Uri.parse(url).getScheme().equals(SCHEME);
    }

    public DisPatcherInfo getTargetClass(String targetUrl) {
        Uri targetUri = Uri.parse(targetUrl);
        String targetHost = targetUri.getHost();
        int pathSegmentSize = targetUri.getPathSegments().size();

        Uri currentUri = null;
        String currentHost = null;
        int currentPathSegmentSize = 0;

        //此处scheme已经校验；放心使用。
        for (String currentCheckUrl : mRealActivityMaps.keySet()) {
            currentUri = Uri.parse(currentCheckUrl);
            currentHost = currentUri.getHost();
            currentPathSegmentSize = currentUri.getPathSegments().size();
            if (TextUtils.equals(currentHost, targetHost) && pathSegmentSize == currentPathSegmentSize) {
                //may optimize
                if (pathSegmentSize > 0 && currentPathSegmentSize > 0
                        && !TextUtils.equals(currentUri.getPathSegments().get(0), targetUri.getPathSegments().get(0))) {
                    break;
                }
                DisPatcherInfo disPatcherInfo = new DisPatcherInfo();
                disPatcherInfo.targetClass = mRealActivityMaps.get(currentCheckUrl);
                disPatcherInfo.matchUrl = currentCheckUrl;
                return disPatcherInfo;
            }
        }
        return null;
    }

}
