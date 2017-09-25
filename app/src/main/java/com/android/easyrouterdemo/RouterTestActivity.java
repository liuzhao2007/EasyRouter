package com.android.easyrouterdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.easyrouter.EasyRouter;
import com.android.easyrouter.annotation.DisPatcher;
import com.android.easyrouter.util.EasyRouterConstant;

/**
 * Created by liuzhao on 2017/9/16.
 */
@DisPatcher("easyrouter://routertest")
public class RouterTestActivity extends FragmentActivity {
    private TextView tv_routertest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routertest);
        tv_routertest = (TextView) findViewById(R.id.tv_routertest);
        tv_routertest.setText("原始Url为：" + getIntent().getStringExtra(EasyRouterConstant.ORIGINALURL));
    }

}
