package com.android.router.dispatcher.dispatcherimpl.moduleinteract;

/**
 * Created by liuzhao on 2017/08/06.
 */
public interface BaseModuleService {

    public interface ModuleInteractService extends BaseModuleService {
        void runModuleInteract();
    }

    public interface AppModuleService extends BaseModuleService {
        void runAppModule();
    }

}
