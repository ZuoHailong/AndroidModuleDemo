package com.hailong.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Display {
    //屏幕的宽度，px
    public static int SCREEN_SAMLL = 240;
    public static int SCREEN_NORMAL = 320;
    public static int SCREEN_LARGE = 480;
    public static int SCREEN_XLARGE = 720;
    public static int SCREEN_XXLARGE = 1080;
    public static int SCREEN_XXXLARGE = 1440;

    //屏幕尺寸
    public static double LARGE_SCREEN_SIZE = 6.5;

    public static float density;
    public static int densityDpi;
    public static int widthPixels;
    public static int heightPixels;
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        densityDpi = dm.densityDpi;
        widthPixels = dm.widthPixels;
        heightPixels = dm.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取是否存在NavigationBar
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    /**
     * 设置手机状态栏颜色
     *
     * @param activity
     * @param colorResId
     */
    public static void setStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//系统，21，5.0
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity_user_info.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dip2Px(float nDip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, nDip, getMetrics());
    }

    public static int sp2Px(float nSp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, nSp, getMetrics());
    }

    public static int px2Dip(float npx) {
        return (int) npx / getMetrics().densityDpi;
    }

    public static int getScreenWidth() {
        return widthPixels;
    }

    public static int getScreenHeight() {
        return heightPixels;
    }

    public static DisplayMetrics getMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        densityDpi = dm.densityDpi;
        widthPixels = dm.widthPixels;
        heightPixels = dm.heightPixels;
        return dm;
    }

    static private WindowManager getWindowManager() {
        return (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
    }

    public static double getScreenSize() {
        DisplayMetrics dm = getMetrics();
        double size = Math.sqrt((Math.pow((dm.widthPixels / dm.xdpi), 2) + Math.pow((dm.heightPixels / dm.ydpi), 2)));
        return size;
    }

    public static boolean portPriority() {
        double size = getScreenSize();
        if (size > 6.9f)
            return true;

        return size >= 4.69f && Math.min(getScreenHeight(), getScreenWidth()) >= 700;
    }

    public static boolean isLargeSize() {
        return getScreenSize() >= LARGE_SCREEN_SIZE && getScreenWidth() > (SCREEN_XXLARGE - 10);
    }

    public static float getFontScale() {
        float scale = 1.0f;
        try {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            try {
                Object am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
                Object config = am.getClass().getMethod("getConfiguration").invoke(am);
                Configuration configs = (Configuration) config;
                scale = configs.fontScale;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            scale = 1.0f;
            e.printStackTrace();
        }
        return scale;
    }

    /**
     * 获取未显示的view的宽高，返回的数组第一个值为宽度，第二个值为高度
     *
     * @param view
     * @return int[]
     */
    public static int[] unDisplayViewSize(View view) {
        int size[] = new int[2];
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        size[0] = view.getMeasuredWidth();
        size[1] = view.getMeasuredHeight();
        return size;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    private static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    /**
     * 修改状态栏为全透明
     * @param activity
     */
    @TargetApi(19)
    public static void transparencyBar(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window =activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
