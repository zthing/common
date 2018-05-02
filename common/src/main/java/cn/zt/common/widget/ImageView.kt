package cn.zt.common.widget

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * @author : #ZhangTao on 2018/5/2 14:18
 */
class ImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    val commonViewDelegate: CommonViewDelegate = CommonViewDelegate(this, attrs)

    override fun onDraw(canvas: Canvas) {
        val saveCount = if (commonViewDelegate.enabledCornerClip)
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        else -1

        super.onDraw(canvas)

        commonViewDelegate.clipCornerRadius(canvas)
        if (saveCount != -1) canvas.restoreToCount(saveCount)
    }
}