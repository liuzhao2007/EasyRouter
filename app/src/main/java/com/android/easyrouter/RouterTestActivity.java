package com.android.easyrouter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.router.annotation.DisPatcher;
import com.android.router.util.EasyRouterConstant;

/**
 * Created by liuzhao on 2017/9/16.
 */
@DisPatcher("easyrouter://routertest")
public class RouterTestActivity extends Activity {
    private TextView tv_routertest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routertest);
        tv_routertest = (TextView) findViewById(R.id.tv_routertest);
        tv_routertest.setText(getIntent().getStringExtra(EasyRouterConstant.ORIGINALURL));
    }
}
