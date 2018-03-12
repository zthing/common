package cn.zt.common.dialog.simple;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.zt.common.utils.ViewUtils;

/**
 * @author : ZhangTao on 2018/1/23 14:00
 */
public abstract class SimpleDialogBuilder<C extends SimpleDialogBuilder> extends AlertDialog.Builder implements SimpleDialogMethod<C> {

    protected SimpleDialogParams mParams;

    protected Context mContext;
    protected AlertDialog mDialog;

    protected abstract C getThis();

    public SimpleDialogParams getBuilderParams() {
        if (mParams == null) {
            params(SimpleDialogBuilder.newParamsBuilder().params());
        }
        return mParams;
    }

    protected <T extends View> T findView(@IdRes int id) {
        return mView.findViewById(id);
    }

    protected View mView;

    private LinearLayout linearContent;
    private LinearLayout linearButton;

    // 存放title
    private CharSequence title;
    private boolean hideCloseView;
    // 存放按钮
    private final LinkedHashMap<CharSequence, SimpleDialogBtnClickListener> MAP_BUTTON = new LinkedHashMap<>();

    public SimpleDialogBuilder(@NonNull Context context) {
        this(context, SimpleDialogBuilder.getDialogParams().themeResId);
    }

    public C hideCloseView(boolean hideCloseView) {
        this.hideCloseView = hideCloseView;
        return getThis();
    }

    public SimpleDialogBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public C params(SimpleDialogParams params) {
        this.mParams = params;

        setCancelable(mParams.cancelable);
        if (mParams.layout > 0) {
            setView(mParams.layout);
            if (mParams.closeId > 0) {
                mView.findViewById(mParams.closeId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
            }
            if (mParams.contentId > 0) {
                linearContent = mView.findViewById(mParams.contentId);
            }
            if (mParams.buttonId > 0) {
                linearButton = mView.findViewById(mParams.buttonId);
            }
        }
        return getThis();
    }

    @Override
    public C setView(View view) {
        super.setView(view);
        this.mView = view;
        return getThis();
    }

    @Override
    public AlertDialog.Builder setView(int layoutResId) {
        return setView(ViewUtils.getView(mContext, layoutResId));
    }

    @Override
    public AlertDialog create() {
        if (mParams == null) {
            params(SimpleDialogBuilder.newParamsBuilder().params());
        }
        if (mView != null) {
            if (mParams.titleId > 0 && title != null) {
                ((TextView) findView(mParams.titleId)).setText(title);
            }
            if (mParams.closeId > 0 && hideCloseView) {
                findView(mParams.closeId).setVisibility(View.GONE);
            }
            if (linearButton != null && MAP_BUTTON.size() > 0 && mParams.btnLayout > 0) {
                int size = MAP_BUTTON.entrySet().size();
                int i = 0;
                for (final Map.Entry<CharSequence, SimpleDialogBtnClickListener> entry : MAP_BUTTON.entrySet()) {
                    View view = ViewUtils.getView(mContext, mParams.btnLayout);
                    ((TextView) view).setText(entry.getKey());
                    linearButton.addView(view, new LinearLayout.LayoutParams(0, -2, 1));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (entry.getValue() != null) {
                                entry.getValue().click(v, mDialog);
                            }
                            if (mParams.clickButtonCancel) {
                                mDialog.cancel();
                            }
                        }
                    });
                    i++;
                    // 设置按钮的背景
                    if (mParams.btnDrawables != null && mParams.btnDrawables.length > 0) {
                        if (size == 1) {
                            view.setBackgroundResource(mParams.btnDrawables[1]);
                        } else if (i == 1) {
                            view.setBackgroundResource(mParams.btnDrawables[0]);
                        } else if (i == size) {
                            view.setBackgroundResource(mParams.btnDrawables[2]);
                        }
                    }
                    // 添加按钮分割线
                    if (mParams.btnLineLayout > 0 && size > 1 && i != size) {
                        LayoutInflater.from(mContext).inflate(mParams.btnLineLayout, linearButton, true);
                    }
                }
            }
        }

        mDialog = super.create();
        mDialog.setCanceledOnTouchOutside(mParams.canceledOnTouchOutside);
        final Window window = mDialog.getWindow();
        if (window != null) {
            if (mParams.hideInput) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
//            mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface dialog) {
//                    WindowManager.LayoutParams params = window.getAttributes();
//                    int screenWidth = ViewUtils.getScreenWidth();
//                    int screenHeight = ViewUtils.getScreenHeight();
//                    if (!ViewUtils.isFullScreen((Activity) mContext)) {
//                        screenHeight = -ViewUtils.getStatusBarHeight();
//                    }
//                    if (mParams.width > 0) {
//                        params.width = mParams.width > 1 ? ViewUtils.dp2px(mParams.width)
//                                : (int) (screenWidth * mParams.width);
//                    }
//                    if (mParams.height > 0) {
//                        params.height = mParams.height > 1 ? ViewUtils.dp2px(mParams.height)
//                                : (int) (screenHeight * mParams.height);
//                    }
//                    if (mParams.minWidth > 0) {
//                        int minWidth = mParams.minWidth > 1 ? ViewUtils.dp2px(mParams.minWidth)
//                                : (int) (screenWidth * mParams.minWidth);
//                        if (params.width < minWidth || (mView != null && mView.getMeasuredWidth() < minWidth)) {
//                            params.width = minWidth;
//                        }
//                    }
//                    if (mParams.minHeight > 0) {
//                        int minHeight = mParams.minHeight > 1 ? ViewUtils.dp2px(mParams.minHeight)
//                                : (int) (screenHeight * mParams.minHeight);
//                        if (params.height < minHeight || (mView != null && mView.getMeasuredHeight() < minHeight)) {
//                            params.height = minHeight;
//                        }
//                    }
//                    if (mParams.maxWidth > 0) {
//                        int maxWidth = mParams.maxWidth > 1 ? ViewUtils.dp2px(mParams.maxWidth)
//                                : (int) (screenWidth * mParams.maxWidth);
//                        if (params.width > maxWidth || (mView != null && mView.getMeasuredWidth() > maxWidth)) {
//                            params.width = maxWidth;
//                        }
//                    }
//                    if (mParams.maxHeight > 0) {
//                        int maxHeight = mParams.maxHeight > 1 ? ViewUtils.dp2px(mParams.maxHeight)
//                                : (int) (screenHeight * mParams.maxHeight);
//                        if (params.height > maxHeight || (mView != null && mView.getMeasuredHeight() > maxHeight)) {
//                            params.height = maxHeight;
//                        }
//                    }
//                    if (mView != null) {
//                        ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
//                        if (layoutParams == null) {
//                            layoutParams = new ViewGroup.LayoutParams(params.width, params.height);
//                            mView.setLayoutParams(layoutParams);
//                        } else {
//                            if (params.width > 0) {
//                                layoutParams.width = params.width;
//                            }
//                            if (params.height > 0) {
//                                layoutParams.height = params.height;
//                            }
//                        }
//                    }
//
//                    if (mParams.windowAnimations > 0) {
//                        window.setWindowAnimations(mParams.windowAnimations);
//                    }
//                    if (mParams.hideNavigation) {
//                        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
//                        }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
//                        }
//                        window.getDecorView().setSystemUiVisibility(uiOptions);
//                    }
//                }
//            });
        }
        return mDialog;
    }

    @Override
    public AlertDialog show() {
        super.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            int screenWidth = ViewUtils.getScreenWidth();
            int screenHeight = ViewUtils.getScreenHeight();
            if (mContext instanceof Activity && !ViewUtils.isFullScreen((Activity) mContext)) {
                screenHeight = -ViewUtils.getStatusBarHeight();
            }
            if (mParams.width > 0) {
                params.width = mParams.width > 1 ? ViewUtils.dp2px(mParams.width)
                        : (int) (screenWidth * mParams.width);
            }
            if (mParams.height > 0) {
                params.height = mParams.height > 1 ? ViewUtils.dp2px(mParams.height)
                        : (int) (screenHeight * mParams.height);
            }
            if (mParams.minWidth > 0) {
                int minWidth = mParams.minWidth > 1 ? ViewUtils.dp2px(mParams.minWidth)
                        : (int) (screenWidth * mParams.minWidth);
                if (params.width < minWidth || (mView != null && mView.getMeasuredWidth() < minWidth)) {
                    params.width = minWidth;
                }
            }
            if (mParams.minHeight > 0) {
                int minHeight = mParams.minHeight > 1 ? ViewUtils.dp2px(mParams.minHeight)
                        : (int) (screenHeight * mParams.minHeight);
                if (params.height < minHeight || (mView != null && mView.getMeasuredHeight() < minHeight)) {
                    params.height = minHeight;
                }
            }
            if (mParams.maxWidth > 0) {
                int maxWidth = mParams.maxWidth > 1 ? ViewUtils.dp2px(mParams.maxWidth)
                        : (int) (screenWidth * mParams.maxWidth);
                if (params.width > maxWidth || (mView != null && mView.getMeasuredWidth() > maxWidth)) {
                    params.width = maxWidth;
                }
            }
            if (mParams.maxHeight > 0) {
                int maxHeight = mParams.maxHeight > 1 ? ViewUtils.dp2px(mParams.maxHeight)
                        : (int) (screenHeight * mParams.maxHeight);
                if (params.height > maxHeight || (mView != null && mView.getMeasuredHeight() > maxHeight)) {
                    params.height = maxHeight;
                }
            }
            if (mView != null) {
                ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(params.width, params.height);
                    mView.setLayoutParams(layoutParams);
                } else {
                    if (params.width > 0) {
                        layoutParams.width = params.width;
                    }
                    if (params.height > 0) {
                        layoutParams.height = params.height;
                    }
                }
            }

            if (mParams.windowAnimations > 0) {
                window.setWindowAnimations(mParams.windowAnimations);
            }
            if (mParams.hideNavigation) {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
                }
                window.getDecorView().setSystemUiVisibility(uiOptions);
            }
        }
        return mDialog;
    }

    private static SimpleDialogParams dialogParams;

    public static SimpleDialogParams.Builder newParamsBuilder() {
        return new SimpleDialogParams.Builder(getDialogParams());
    }

    private static SimpleDialogParams getDialogParams() {
        if (dialogParams == null) {
            dialogParams = new SimpleDialogParams.Builder().params();
        }
        return dialogParams;
    }

    public static void setDialogParams(SimpleDialogParams dialogConfig) {
        SimpleDialogBuilder.dialogParams = dialogConfig;
    }

    @Override
    public C title(CharSequence sequence) {
        this.title = sequence;
        return getThis();
    }

    @Override
    public C addView(View view, LinearLayout.LayoutParams params) {
        if (mParams == null) {
            params(SimpleDialogBuilder.newParamsBuilder().params());
        }
        if (linearContent != null) {
            if (params == null) {
                linearContent.addView(view);
            } else {
                linearContent.addView(view, params);
            }
        }
        return getThis();
    }

    @Override
    public C addView(View view) {
        return addView(view, null);
    }

    @Override
    public C addView(int layout) {
        return addView(layout, null);
    }

    @Override
    public C addView(int layout, LinearLayout.LayoutParams params) {
        return addView(ViewUtils.getView(mContext, layout), params);
    }

    @Override
    public C addButton(CharSequence btn) {
        return addButton(btn, null);
    }

    @Override
    public C addButton(CharSequence btn, SimpleDialogBtnClickListener listener) {
        MAP_BUTTON.put(btn, listener);
        return getThis();
    }
}