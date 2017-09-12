package com.android.router.dispatcherimpl.moduleinteract;

/**
 * Created by liuzhao on 2017/08/06.
 */
public interface BaseModuleService {

    public interface UserModuleService extends BaseModuleService {
        void getUserName();
    }

    public interface AppModuleService extends BaseModuleService {
        void switchRole();
    }


}
