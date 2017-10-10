package com.android.easyrouterdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.android.easyrouter.EasyRouter;
import com.android.easyrouter.annotation.AutoAssign;
import com.android.easyrouter.annotation.DisPatcher;
import com.android.easyrouter.util.EasyRouterConstant;

import static com.android.easyrouter.util.EasyRouterConstant.ORIGINALURL;

/**
 * Created by liuzhao on 2017/9/16.
 */
@DisPatcher("easyrouter://routertest")
public class RouterTestActivity extends FragmentActivity {
    private TextView tv_routertest;
    private TextView tv_params;
    @AutoAssign
    long time;
    @AutoAssign
    int age;
    @AutoAssign
    double money;
    @AutoAssign
    String name;
    @AutoAssign
    String company;
    @AutoAssign
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routertest);
        EasyRouter.inject(this);
        tv_params = (TextView) findViewById(R.id.tv_params);
        tv_routertest = (TextView) findViewById(R.id.tv_routertest);
//        tv_params.setText("传递的参数为：姓名：" + getIntent().getStringExtra("name") + "，公司：" + getIntent().getStringExtra("company"));
//        tv_routertest.setText("原始Url为：" + getIntent().getStringExtra(EasyRouterConstant.ORIGINALURL));


        tv_params.setText("传递的参数为：姓名：" + name + "，公司：" + company);
        tv_routertest.setText("原始Url为：" + url);
    }

}
