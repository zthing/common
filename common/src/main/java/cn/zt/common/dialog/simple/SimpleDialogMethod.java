package cn.zt.common.dialog.simple;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author : ZhangTao on 2018/1/23 18:18
 */
public interface SimpleDialogMethod<C extends SimpleDialogBuilder> {
    C title(CharSequence sequence);

    C addButton(CharSequence btn);

    C addButton(CharSequence btn, SimpleDialogBtnClickListener listener);

    C addView(View view, LinearLayout.LayoutParams params);

    C addView(View view);

    C addView(@LayoutRes int layout);

    C addView(@LayoutRes int layout, LinearLayout.LayoutParams params);
}