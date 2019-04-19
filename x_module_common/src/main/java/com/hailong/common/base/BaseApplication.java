package com.hailong.common.base;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hailong.common.BuildConfig;

import androidx.multidex.MultiDex;

/**
 * Describe：基础Application，所有需要模块化开发的module都需要继承自此BaseApplication。
 * Created by ZuoHailong on 2019/4/11.
 */
public class BaseApplication extends Application {

    //全局唯一的context
    private static BaseApplication application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
        //MultiDexf分包初始化，必须最先初始化
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initARouter();
    }

    /**
     * 初始化路由
     */
    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();  // 打印日志
            ARouter.openDebug(); // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application);// 尽可能早，推荐在Application中初始化
    }

    /**
     * 获取全局唯一上下文
     *
     * @return BaseApplication
     */
    public static BaseApplication getApplication() {
        return application;
    }

}
