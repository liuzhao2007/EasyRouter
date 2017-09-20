package com.android.easyrouter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.router.annotation.DisPatcher;
import com.android.router.callback.DefaultRouterCallBack;
import com.android.router.EasyRouter;
import com.android.router.intercept.IInterceptor;
import com.android.router.util.LogUtil;
import com.easyrouter.service.BaseModuleService;

/**
 * Created by liuzhao on 2017/9/13.
 */
@DisPatcher({"easyrouter://main", "easyrouter://maintwo"})
public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easyrouter_maintest);
        findViewById(R.id.bt_normaljump).setOnClickListener(this);
        findViewById(R.id.bt_jumpwithinteractor).setOnClickListener(this);
        findViewById(R.id.bt_callservice).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_normaljump:
                // Example for normal jump
                EasyRouter.open("easyrouter://routertest", new DefaultRouterCallBack());
                break;

            case R.id.bt_jumpwithinteractor:
                // Example for intercept
                EasyRouter.with("easyrouter://routertest").addInterceptor(new IInterceptor() {
                    @Override
                    public boolean intercept() {
                        LogUtil.i("check if intercept");
                        return false;
                    }

                    @Override
                    public void onIntercepted() {
                        LogUtil.i("onIntercepted");
                    }
                }).open();
                break;

            case R.id.bt_callservice:
                // Example for service invoke
                EasyRouter.getModuleService(BaseModuleService.ModuleInteractService.class).runModuleInteract();
                break;
        }
    }
}
