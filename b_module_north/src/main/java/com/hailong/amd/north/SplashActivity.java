package com.hailong.amd.north;

import android.os.Bundle;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hailong.common.base.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Describe：
 * Created by ZuoHailong on 2019/4/19.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R2.id.btn_toMain)
    Button btnToMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_module_north_activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        actionBar.setLeftGone();
        actionBar.setCenterText("北方大区 组件");
    }

    @Override
    protected void initData() {

    }

    @OnClick(R2.id.btn_toMain)
    public void onViewClicked() {
        ARouter.getInstance()
                .build("/main/MainActivity")
                .navigation();
    }
}
