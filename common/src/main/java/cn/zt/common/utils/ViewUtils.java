package cn.zt.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import cn.zt.common.Common;

/**
 * @author : ZhangTao on 2018/1/25 12:06
 */
public class ViewUtils {

    public static View getView(Context context, @LayoutRes int layout, ViewGroup root) {
        return LayoutInflater.from(context).inflate(layout, root, root == null);
    }

    public static View getView(Context context, @LayoutRes int layout) {
        return getView(context, layout, null);
    }

    public static int getScreenWidth() {
        return Common.getApp().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Common.getApp().getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(final float dpValue) {
        final float scale = Common.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getStatusBarHeight() {
        Resources resources = Common.getApp().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static boolean isFullScreen(final Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        return (flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }
}