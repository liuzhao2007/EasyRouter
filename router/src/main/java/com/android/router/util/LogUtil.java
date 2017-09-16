package com.android.router.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil {
    private static String DEFAULT_TAG = "easyrouter";


    private static boolean debug = true;

    private static String getMessage(String msg) {
        StackTraceElement sts = new Exception().getStackTrace()[2];
        if (sts == null) {
            return null;
        }
        if (!TextUtils.isEmpty(sts.toString())) {
            return sts.getFileName() + "--->" + sts.getMethodName() + "():" + sts.getLineNumber() + "*****" + msg;
        }
        return null;
    }


    public static void d(String tag, String msg) {
        if (!debug) {
            return;
        }
        Log.d(tag, getMessage(msg));
    }

    public static void d(String msg) {
        if (!debug) {
            return;
        }
        Log.d(DEFAULT_TAG, getMessage(msg));
    }

    public static void v(String tag, String msg) {
        if (!debug) {
            return;
        }
        Log.v(tag, getMessage(msg));
    }

    public static void v(String msg) {
        if (!debug) {
            return;
        }
        Log.v(DEFAULT_TAG, getMessage(msg));
    }

    public static void i(String tag, String msg) {
        if (!debug) {
            return;
        }
        Log.i(tag, getMessage(msg));
    }

    public static void i(String msg) {
        if (!debug) {
            return;
        }
        Log.i(DEFAULT_TAG, getMessage(msg));
    }

    public static void e(String tag, String msg) {
        if (!debug) {
            return;
        }
        Log.e(tag, getMessage(msg));
    }


    public static void i(Exception ex) {
        if (!debug) {
            return;
        }
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        String string = writer.toString();
        Log.i(DEFAULT_TAG, string);
    }

    public static void e(Exception ex) {
        if (!debug) {
            return;
        }
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        String string = writer.toString();
        Log.e(DEFAULT_TAG, string);
    }


    public static void e(String msg) {
        if (!debug) {
            return;
        }
        Log.e(DEFAULT_TAG, getMessage(msg));
    }

    public static void w(String tag, String msg) {
        if (!debug) {
            return;
        }
        Log.w(tag, getMessage(msg));
    }

    public static void w(String msg) {
        if (!debug) {
            return;
        }
        Log.w(DEFAULT_TAG, getMessage(msg));
    }

    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }
}
