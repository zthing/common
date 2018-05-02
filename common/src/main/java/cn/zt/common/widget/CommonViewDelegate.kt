package cn.zt.common.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import cn.zt.common.R

/**
 * @author : #ZhangTao on 2018/5/2 10:05
 */
class CommonViewDelegate(val view: View, attrs: AttributeSet?) {
    private val cornerClipPath = Path()
    private val cornerClipPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gdBackground = GradientDrawable()
    private val gdBackgroundPress = GradientDrawable()
    private val radiusArray = FloatArray(8)

    var enabledCornerClip = false
        set(value) {
            if (field != value) {
                field = value
                view.postInvalidate()
            }
        }
    fun setCornerRadius(cornerRadius:Int){
        cornerRadiusTL = cornerRadius
        cornerRadiusTR = cornerRadius
        cornerRadiusBL = cornerRadius
        cornerRadiusBL = cornerRadius
        setBackground()
        if (enabledCornerClip) view.postInvalidate()
    }
    var cornerRadiusTL = -1
        set(value) {
            if (field != value) {
                field = value
                setBackground()
                if (enabledCornerClip) view.postInvalidate()
            }
        }
    var cornerRadiusTR = -1
        set(value) {
            if (field != value) {
                field = value
                setBackground()
                if (enabledCornerClip) view.postInvalidate()
            }
        }
    var cornerRadiusBL = -1
        set(value) {
            if (field != value) {
                field = value
                setBackground()
                if (enabledCornerClip) view.postInvalidate()
            }
        }
    var cornerRadiusBR = -1
        set(value) {
            if (field != value) {
                field = value
                setBackground()
                if (enabledCornerClip) view.postInvalidate()
            }
        }

    var strokeWidth = 0
        set(value) {
            if (field != value) {
                field = value
                setBackground()
            }
        }
    var strokeColor = 0
        set(value) {
            if (field != value) {
                field = value
                setBackground()
            }
        }
    var strokeColorPress = 0
        set(value) {
            if (field != value) {
                field = value
                setBackground()
            }
        }
    var backgroundColor = 0
        set(value) {
            if (field != value) {
                field = value
                setBackground()
            }
        }
    var backgroundColorPress = 0
        set(value) {
            if (field != value) {
                field = value
                setBackground()
            }
        }
    var enabledRipple = false
        set(value) {
            if (field != value) {
                field = value
                setBackground()
            }
        }

    var elevation = 0
        set(value) {
            field = value
            setElevation()
        }

    init {
        cornerClipPaint.color = Color.WHITE
        cornerClipPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)

        val osa = view.context.obtainStyledAttributes(attrs, R.styleable.CommonViewDelegate)
        enabledCornerClip = osa.getBoolean(R.styleable.CommonViewDelegate_common_enabledCornerClip, enabledCornerClip)
        val cornerRadius = osa.getDimensionPixelSize(R.styleable.CommonViewDelegate_common_cornerRadius, -1)
        cornerRadiusTL = osa.getDimensionPixelSize(R.styleable.CommonViewDelegate_common_cornerRadiusTL, cornerRadius)
        cornerRadiusTR = osa.getDimensionPixelSize(R.styleable.CommonViewDelegate_common_cornerRadiusTR, cornerRadius)
        cornerRadiusBL = osa.getDimensionPixelSize(R.styleable.CommonViewDelegate_common_cornerRadiusBL, cornerRadius)
        cornerRadiusBR = osa.getDimensionPixelSize(R.styleable.CommonViewDelegate_common_cornerRadiusBR, cornerRadius)
        strokeWidth = osa.getDimensionPixelSize(R.styleable.CommonViewDelegate_common_strokeWidth, strokeWidth)
        strokeColor = osa.getColor(R.styleable.CommonViewDelegate_common_strokeColor, strokeColor)
        strokeColorPress = osa.getColor(R.styleable.CommonViewDelegate_common_strokeColorPress, strokeColorPress)
        elevation = osa.getDimensionPixelSize(R.styleable.CommonViewDelegate_common_elevation, elevation)
        backgroundColor = osa.getColor(R.styleable.CommonViewDelegate_common_backgroundColor, backgroundColor)
        backgroundColorPress = osa.getColor(R.styleable.CommonViewDelegate_common_backgroundColorPress, backgroundColorPress)
        enabledRipple = osa.getBoolean(R.styleable.CommonViewDelegate_common_enabledRipple, enabledRipple)
        osa.recycle()

        setBackground()
        setElevation()
    }

    private fun setBackground() {
        setDrawable(gdBackground, backgroundColor, strokeColor)
        val drawable: Drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && enabledRipple) {
            RippleDrawable(getPressedColorSelector(backgroundColor, backgroundColorPress), gdBackground, null)
        } else {
            val bg = StateListDrawable()
            if (backgroundColorPress != 0 || strokeColorPress != 0) {
                setDrawable(gdBackgroundPress, if (backgroundColorPress == 0) backgroundColor else backgroundColorPress,
                        if (strokeColorPress == 0) strokeColor else strokeColorPress)
                bg.addState(intArrayOf(android.R.attr.state_pressed), gdBackgroundPress)
            }
            bg.addState(intArrayOf(), gdBackground)
            bg
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

    private fun setElevation() {
        ViewCompat.setElevation(view, elevation.toFloat())
        val animator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator()
        } else null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator?.let {
                val animatorSetNormal = AnimatorSet()
                animatorSetNormal.play(ObjectAnimator.ofFloat(view, "elevation", view.elevation, elevation.toFloat()))
                val animatorSetPress = AnimatorSet()
                animatorSetPress.play(ObjectAnimator.ofFloat(view, "elevation", view.elevation, elevation * 3 + 3f))
                it.addState(intArrayOf(android.R.attr.state_pressed), animatorSetPress)
                it.addState(intArrayOf(), animatorSetNormal)
                view.stateListAnimator = it
            }
        }
    }

    private fun setDrawable(gd: GradientDrawable, color: Int, strokeColor: Int) {
        gd.setColor(color)
        if (cornerRadiusTL > -1) {
            radiusArray[0] = cornerRadiusTL.toFloat()
            radiusArray[1] = cornerRadiusTL.toFloat()
        }
        if (cornerRadiusTR > -1) {
            radiusArray[2] = cornerRadiusTR.toFloat()
            radiusArray[3] = cornerRadiusTR.toFloat()
        }
        if (cornerRadiusBR > -1) {
            radiusArray[4] = cornerRadiusBR.toFloat()
            radiusArray[5] = cornerRadiusBR.toFloat()
        }
        if (cornerRadiusBL > -1) {
            radiusArray[6] = cornerRadiusBL.toFloat()
            radiusArray[7] = cornerRadiusBL.toFloat()
        }
        gd.cornerRadii = radiusArray

        gd.setStroke(strokeWidth, if (strokeColor == 0) color else strokeColor)
    }

    private fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList {
        return ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf(android.R.attr.state_focused)
                        , intArrayOf(android.R.attr.state_activated), intArrayOf())
                , intArrayOf(pressedColor, pressedColor, pressedColor, normalColor)
        )
    }

    fun clipCornerRadius(canvas: Canvas?) {
        if (canvas != null && enabledCornerClip) {
            cornerClipPath.reset()
            cornerClipPath.addRoundRect(RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()), radiusArray, Path.Direction.CW)
            canvas.drawPath(cornerClipPath, cornerClipPaint)
        }
    }
}