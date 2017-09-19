package com.android.router.dispatcher.dispatcherimpl.moduleinteract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzhao on 2017/08/06.
 * 各个Module之间交互的管理者
 */
public class ModuleServiceManager {


    private static Map<Class<? extends IBaseModuleService>, IBaseModuleService> moduleInteracts = new HashMap<Class<? extends IBaseModuleService>, IBaseModuleService>();

    public static <T extends IBaseModuleService> T getModuleService(Class<T> tClass) {
        if (moduleInteracts.containsKey(tClass)) {
            return (T) moduleInteracts.get(tClass);
        }
        return null;
    }

    public static void register(Class<? extends IBaseModuleService> bClass, IBaseModuleService baseInteract) {
        moduleInteracts.put(bClass, baseInteract);
    }


}
