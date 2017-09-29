package com.android.easyrouterdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
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
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easyrouter_maintest);
        findViewById(R.id.bt_normaljump).setOnClickListener(this);
        findViewById(R.id.bt_jumpwithinteractor).setOnClickListener(this);
        findViewById(R.id.bt_callservice).setOnClickListener(this);
        findViewById(R.id.bt_addfragment).setOnClickListener(this);
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
            case R.id.bt_addfragment:
                Fragment fragment = EasyRouter.with("easyrouter://fragmenttest").getFragment(Fragment.class);
                MainActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment).commit();
                break;
        }
    }


}
