package com.android.easyrouterdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.easyrouter.EasyRouter;
import com.android.easyrouter.callback.DefaultRouterCallBack;
import com.android.easyrouter.intercept.IInterceptor;
import com.android.easyrouter.service.BaseModuleService;
import com.android.easyrouter.annotation.DisPatcher;
import com.android.easyrouter.util.LogUtil;

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
                        Toast.makeText(getApplicationContext(), "Router is intercepted by me ", 1).show();
                        return true;
                    }

                    @Override
                    public void onIntercepted() {
                        LogUtil.i("onIntercepted");
                    }
                }).open();
                break;

            case R.id.bt_callservice:
                // Example for service invoke
                EasyRouter.getModuleService(BaseModuleService.ModuleInteractService.class).runModuleInteract(MainActivity.this);
                break;
        }
    }


}
