package com.hailong.amd.main;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hailong.common.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * Describe：
 * Created by ZuoHailong on 2019/4/19.
 */
@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_module_main_activity_main);
    }

    @Override
    protected void initView() {
        actionBar.setCenterText("Main组件");
    }

    @Override
    protected void initData() {

    }
}
