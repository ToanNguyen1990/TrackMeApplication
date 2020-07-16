package net.toannt.hacore.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

class HomaEditText : AppCompatEditText {

    private var onActionPerformClicked: OnActionClickListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context) {
        setOnTouchListener { _, event ->
            try {

                if (event.action == MotionEvent.ACTION_UP) {
                    val rect: Rect
                    var isClickedInDrawableZone = false
                    if (IS_RTL) {
                        if (compoundDrawables[DRAWABLE_LEFT] != null) {
                            rect = compoundDrawables[DRAWABLE_LEFT].bounds
                            isClickedInDrawableZone = event.rawX <= rect.width() * 3
                        }
                    } else {
                        if (compoundDrawables[DRAWABLE_RIGHT] != null) {
                            rect = compoundDrawables[DRAWABLE_RIGHT].bounds
                            isClickedInDrawableZone = event.rawX >= right - rect.width() * 3
                        }
                    }
                    if (isClickedInDrawableZone) {
                        if (onActionPerformClicked != null)
                            onActionPerformClicked!!.onActionClicked(text.toString())
                        else
                            setText("")
                        return@setOnTouchListener true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }
    }

    fun setOnActionClickListener(onActionPerformClicked: OnActionClickListener) {
        this.onActionPerformClicked = onActionPerformClicked
    }

    interface OnActionClickListener {
        fun onActionClicked(text: String?)
    }

    companion object {
        const val DRAWABLE_LEFT = 0
        const val DRAWABLE_TOP = 1
        const val DRAWABLE_RIGHT = 2
        const val DRAWABLE_BOTTOM = 3
        const val IS_RTL = false
    }
}
