package com.android.easyrouter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.router.annotation.DisPatcher;
import com.android.router.callback.DefaultRouterCallBack;
import com.android.router.dispatcher.dispatcherimpl.EasyRouter;
import com.android.router.dispatcher.dispatcherimpl.moduleinteract.ModuleServiceManager;
import com.android.router.intercept.IInterceptor;
import com.android.router.util.LogUtil;
import com.easyrouter.service.BaseModuleService;

/**
 * Created by liuzhao on 2017/9/13.
 */
@DisPatcher("easyrouter://main")
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainact);
        findViewById(R.id.bt_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EasyRouter.open("easyrouter://routertest",
//                        new DefaultRouterCallBack());


                // Example for intercept
//                EasyRouter.with("easyrouter://routertest").addInterceptor(new IInterceptor() {
//                    @Override
//                    public boolean intercept() {
//                        LogUtil.i("if intercept");
//                        return true;
//                    }
//
//                    @Override
//                    public void onIntercepted() {
//                        LogUtil.i("onIntercepted");
//                    }
//                }).open();

                // Example for service
                EasyRouter.getModuleService(BaseModuleService.ModuleInteractService.class).runModuleInteract();

            }
        });

    }
}
