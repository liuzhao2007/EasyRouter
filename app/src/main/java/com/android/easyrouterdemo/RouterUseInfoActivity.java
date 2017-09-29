package com.android.easyrouterdemo;

import android.app.Activity;
import android.os.Bundle;

import com.android.easyrouter.annotation.DisPatcher;

/**
 * Created by liuzhao on 2017/9/29.
 */

@DisPatcher("easyrouter://routeruseinfo")
public class RouterUseInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easyrouter_useinfo);

    }
}
