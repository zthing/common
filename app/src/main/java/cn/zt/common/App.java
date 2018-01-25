package cn.zt.common;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * @author : ZhangTao on 2018/1/25 13:33
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Common.init(this);
        Utils.init(this);
    }
}
