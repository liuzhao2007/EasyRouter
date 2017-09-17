package com.android.easyrouter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.router.annotation.DisPatcher;
import com.android.router.callback.DefaultRouterCallBack;
import com.android.router.dispatcherimpl.EasyRouter;

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
                EasyRouter.open("easyrouter://routertest",
                        new DefaultRouterCallBack());
            }
        });

    }
}
