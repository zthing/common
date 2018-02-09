package cn.zt.common.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * @author : ZhangTao on 2018/2/8 14:09
 */

public class TypefaceTextView extends AppCompatTextView {
    private static Typeface typeface;

    public TypefaceTextView(Context context) {
        super(context);
        init();
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "font/fzkt.ttf");
        }
        setTypeface(typeface);
    }
}
