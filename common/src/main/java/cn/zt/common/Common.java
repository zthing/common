package cn.zt.common;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * @author : ZhangTao on 2018/1/25 12:08
 */
public class Common {
    @SuppressLint("StaticFieldLeak")
    private static Application application;

    public static void init(Application application) {
        Common.application = application;
    }

    public static Application getApp() {
        if (application != null) return application;
        throw new NullPointerException("you should init the Common first");
    }
}