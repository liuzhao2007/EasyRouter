package com.android.router.moduleinteract;

import android.content.Context;
import android.widget.Toast;

import com.android.easyrouter.service.BaseModuleService;
import com.android.router.annotation.ModuleService;
import com.android.router.util.LogUtil;

/**
 * Created by liuzhao on 2017/9/18.
 */
@ModuleService
public class ModuleInteractService implements BaseModuleService.ModuleInteractService {

    @Override
    public void runModuleInteract(Context context) {
        Toast.makeText(context, "ModuleInteractService 服务调用成功！", 1).show();
        LogUtil.i("runModuleInteract");
    }
}
