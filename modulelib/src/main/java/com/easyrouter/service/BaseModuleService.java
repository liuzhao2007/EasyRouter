package com.easyrouter.service;

import com.android.router.dispatcher.dispatcherimpl.moduleinteract.IBaseModuleService;

/**
 * Created by liuzhao on 2017/9/19.
 */

public interface BaseModuleService extends IBaseModuleService {

    public interface ModuleInteractService extends BaseModuleService {
        void runModuleInteract();
    }

    public interface AppModuleService extends BaseModuleService {
        void runAppModule();
    }
}
