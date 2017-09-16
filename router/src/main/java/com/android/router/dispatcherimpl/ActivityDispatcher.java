package com.android.router.dispatcherimpl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;


import com.android.router.dispatcherimpl.model.DisPatcherInfo;
import com.android.router.dispatcherimpl.model.IntentWraper;
import com.android.router.idispatcher.IActivityDispatcher;
import com.android.router.idispatcher.IActivityInitMap;
import com.android.router.util.LogUtil;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by liuzhao on 16/10/21.
 */
public class ActivityDispatcher implements IActivityDispatcher {

    private static ActivityDispatcher activityDispatcher;
    public static String SCHEME = "easyrouter";
    private int DEFAULTVALUE = -1;
    public HashMap<String, Class<? extends Activity>> activityMaps = new HashMap<String, Class<? extends Activity>>();

    private static final String PARAM_URL = "url";//获取需要编码的url
    private static final String CHARSET = "UTF-8";//编码的字符集

    private ActivityDispatcher() {
    }

    public static ActivityDispatcher getActivityDispatcher() {
        if (activityDispatcher == null) {
            synchronized (ActivityDispatcher.class) {
                if (activityDispatcher == null) {
                    activityDispatcher = new ActivityDispatcher();
                }
            }
        }
        return activityDispatcher;
    }

    public void initActivityMaps(IActivityInitMap activityInitMap) {
        activityInitMap.initActivityMap(activityMaps);
    }

    public boolean open(String url) {
        return open(null, url);
    }

    public boolean open(Activity activity, String url) {
        return realOpen(activity, url, null);
    }

    public IntentWraper withUrl(String string) {
        return new IntentWraper(string);
    }

    public void open(Activity activity, IntentWraper intentWraper) {
        realOpen(activity, intentWraper.mUrl, intentWraper);
    }

    private boolean realOpen(Activity activity, String url, IntentWraper intentWraper) {
        try {
            if (TextUtils.isEmpty(url) || !canOpen(url)) {
                return false;
            }
            //need to redirect

            url = encodeUrl(url);
            DisPatcherInfo disPatcherInfo = getTargetClass(url);
            if (disPatcherInfo == null) {
                return false;
            }
            if (intentWraper == null) {
                intentWraper = new IntentWraper();
            }
            Intent intent = new Intent(activity == null ? EasyRouter.mApplication : activity, disPatcherInfo.targetClass);
            intent = setParams(intent, url, disPatcherInfo.matchUrl);
            intent.putExtras(intentWraper.mBundle);
            if (intentWraper.mIntentFlag != DEFAULTVALUE) {
                intent.addFlags(intentWraper.mIntentFlag);
            }
            if (activity == null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                EasyRouter.mApplication.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, intentWraper.mRequestCode);
                if (activity != null && intentWraper.mInAnimation != DEFAULTVALUE && intentWraper.mOutAnimation != DEFAULTVALUE) {
                    activity.overridePendingTransition(intentWraper.mInAnimation, intentWraper.mOutAnimation);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e);
            return false;
        }
        return true;
    }

    //    ActivityDispatcher.registerDis("chinahr://customer/second/i:tab/b:flag", SecondActivity.class);
    //     chinahr://customer/second/20/true
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
                    paramsType = string.substring(0, 1);//参数类型；
                    paramsName = string.substring(2, string.length());//参数名称；
                }
                switch (paramsType) {
                    case "i":
                        //说明是int类型；
                        try {
                            int intParamValue = Integer.parseInt(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, intParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, -100);
                        }
                        break;
                    case "f":
                        //说明是float类型；
                        try {
                            float floatParamValue = Float.parseFloat(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, floatParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, 0.0f);
                        }
                        break;
                    case "b":
                        //说明是boolean类型；
                        try {
                            boolean booleanParamValue = Boolean.parseBoolean(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, booleanParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, false);
                        }
                        break;
                    case "d":
                        //说明是double类型；
                        try {
                            double doubleParamValue = Double.parseDouble(targetSegments.get(segments.indexOf(string)));
                            intent.putExtra(paramsName, doubleParamValue);
                        } catch (Exception e) {
                            intent.putExtra(paramsName, 0.0d);
                        }
                        break;
                    case "s":
                        //说明是string类型；
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
     * 对Url进行编码；规则的调整。
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

    /**
     * 特殊处理对于Fragment的跳转
     *
     * @param url
     * @param suffix
     * @return
     */
    public static String dealFragment(String url, String suffix) {
        if (url.contains("?")) {
            String[] strings = url.split("\\?");
            return strings != null && strings.length == 2 ? strings[0] + suffix + "?" + strings[1] : url + suffix;
        } else {
            return url + suffix;
        }
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
        for (String currentUrl : activityMaps.keySet()) {
            currentUri = Uri.parse(currentUrl);
            currentHost = currentUri.getHost();
            currentPathSegmentSize = currentUri.getPathSegments().size();
            if (TextUtils.equals(currentHost, targetHost) && pathSegmentSize == currentPathSegmentSize) {
                //此处有优化空间。
                if (pathSegmentSize > 0 && currentPathSegmentSize > 0
                        && !TextUtils.equals(currentUri.getPathSegments().get(0), targetUri.getPathSegments().get(0))) {
                    break;
                }
                DisPatcherInfo disPatcherInfo = new DisPatcherInfo();
                disPatcherInfo.targetClass = activityMaps.get(currentUrl);
                disPatcherInfo.matchUrl = currentUrl;
                return disPatcherInfo;
            }
        }
        return null;
    }

}
