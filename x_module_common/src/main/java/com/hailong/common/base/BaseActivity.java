package com.hailong.common.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hailong.common.R;
import com.hailong.common.arouter.service.ISplashService;
import com.hailong.common.view.ActionBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * Describe：所有Activity的基类
 * Created by ZuoHailong on 2019/4/12.
 */

public abstract class BaseActivity extends FragmentActivity {

    public ActionBar actionBar;
    private FrameLayout container;

    private ISplashService splashService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.x_module_common_activity_base);

        actionBar = findViewById(R.id.actionbar);
        container = findViewById(R.id.fl_container);
        actionBar.setLeftIcon(R.mipmap.x_module_common_ic_back_white, view -> {
                    finish();
                }
        );

    }

    /**
     * 供子类调用
     */
    @Override
    public void setContentView(int layoutResID) {
        View childView = getLayoutInflater().inflate(layoutResID, null);
        if (container == null) {
            splashService = ARouter.getInstance().navigation(ISplashService.class);
            splashService.toSplash();
            finish();
            return;
        }
        container.addView(childView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    /**
     * 隐藏actionBar
     */
    public void setActionBarGone() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();
}
