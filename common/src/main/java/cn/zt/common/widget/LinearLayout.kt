package cn.zt.common.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout


/**
 * @author : #ZhangTao on 2018/5/4 15:32
 */
class LinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    val commonViewDelegate = CommonViewDelegate(this, attrs)

    override fun onDraw(canvas: Canvas) {
        val saveCount = if (commonViewDelegate.enabledCornerClip)
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        else -1

        super.onDraw(canvas)

        commonViewDelegate.clipCornerRadius(canvas)
        if (saveCount != -1) canvas.restoreToCount(saveCount)
    }
}