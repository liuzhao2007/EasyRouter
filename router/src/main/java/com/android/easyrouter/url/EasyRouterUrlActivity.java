package com.android.easyrouter.url;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.android.easyrouter.EasyRouter;

/**
 * Created by liuzhao on 2017/10/10.
 */

public class EasyRouterUrlActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            EasyRouter.open(this, uri.toString());
        }
        finish();
    }
}
