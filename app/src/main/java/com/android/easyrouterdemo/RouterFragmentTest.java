package com.android.easyrouterdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.easyrouter.annotation.DisPatcher;

/**
 * Created by liuzhao on 2017/9/19.
 */

@DisPatcher("easyrouter://fragmenttest")
public class RouterFragmentTest extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.easyrouter_fragment, null);
    }
}
