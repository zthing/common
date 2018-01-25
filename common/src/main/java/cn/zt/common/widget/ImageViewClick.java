package cn.zt.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author : ZhangTao on 2018/1/16 15:58
 */
public class ImageViewClick extends AppCompatImageView {
    public ImageViewClick(Context context) {
        super(context);
    }

    public ImageViewClick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewClick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setColorFilter(0x55000000);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setColorFilter(null);
                break;
        }
        return super.onTouchEvent(event);
    }
}
