package com.android.router.moduleinteract;

import com.android.router.annotation.ModuleService;
import com.android.router.util.LogUtil;
import com.easyrouter.service.BaseModuleService;

/**
 * Created by liuzhao on 2017/9/18.
 */
@ModuleService
public class ModuleInteractService implements BaseModuleService.ModuleInteractService {

    @Override
    public void runModuleInteract() {
        LogUtil.i("runModuleInteract");
    }
}
