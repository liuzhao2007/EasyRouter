package com.android.easyrouter.compiler.constant;

/**
 * Created by liuzhao on 2017/7/29.
 */

public class CompilerConstant {
    public static final String KEY_MODULE_NAME = "moduleName";
    public static final String AutoCreateActivityMapPrefix = "AutoCreateActivityMap_";
    public static final String AutoCreateInterceptorPrefix = "AutoCreateInterceptor_";
    public static final String AutoCreateActivityMapMethod = ".initRouterTable()";
    public static final String AutoCreateDispatcherPackage = "com.android.easyrouter";
    public static final String AutoCreateInterceptorPackage = "com.android.easyrouter.interceptor";

    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String STRING = LANG + ".String";
}
