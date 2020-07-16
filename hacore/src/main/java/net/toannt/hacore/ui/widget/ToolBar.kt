package net.toannt.hacore.ui.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.homa.app.demo.hacore.R


class ToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var tvTitle: TextView? = null
    lateinit var tvTitleRight: TextView
    lateinit var buttonLeft: AppCompatImageView
    lateinit var buttonRight: AppCompatImageView

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null)
            return
        val array = context.obtainStyledAttributes(attrs, R.styleable.ToolBar)
        try {
            val layout = array.getInteger(R.styleable.ToolBar_layoutType, 0)
            View.inflate(
                context,
                if (layout == 0) R.layout.layout_tool_bar else R.layout.layout_tool_bar_lr,
                this
            )
            if (array.hasValue(R.styleable.ToolBar_hm_background)) {
                setBackgroundColor(
                    array.getColor(
                        R.styleable.ToolBar_hm_background,
                        resources.getColor(android.R.color.transparent)
                    )
                )
            }
            tvTitle = findViewById(R.id.text_view)
            tvTitleRight = findViewById(R.id.text_view_right)
            buttonLeft = findViewById(R.id.button_left)
            buttonRight = findViewById(R.id.button_right)
            tvTitle!!.setTextColor(
                array.getColor(
                    R.styleable.ToolBar_titleColor,
                    resources.getColor(android.R.color.white)
                )
            )
            tvTitleRight.setTextColor(
                array.getColor(
                    R.styleable.ToolBar_titleRightColor,
                    resources.getColor(android.R.color.white)
                )
            )
            val title = array.getString(R.styleable.ToolBar_title)
            if (!TextUtils.isEmpty(title))
                setTitleAndHideDrawables(title)
            val titleRight = array.getString(R.styleable.ToolBar_titleRight)
            if (!TextUtils.isEmpty(titleRight))
                setTitleRight(titleRight)
            val textSize = array.getDimension(R.styleable.ToolBar_titleSize, -1f)
            if (textSize != -1f) {
                tvTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            val textRightSize = array.getDimension(R.styleable.ToolBar_titleRightSize, -1f)
            if (textRightSize != -1f) {
                tvTitleRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, textRightSize)
            }
            val l = array.getResourceId(R.styleable.ToolBar_hm_drawableLeft, -1)
            val r = array.getResourceId(R.styleable.ToolBar_hm_drawableRight, -1)
            if (l != -1)
                setDrawableLeft(l)
            if (r != -1)
                setDrawableRight(r)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            array.recycle()
        }
    }

    fun setTitle(title: String) {
        setTitle(title, true)
    }

    private fun setTitleAndHideDrawables(title: String?) {
        tvTitle!!.text = title
        setBinding(Integer.MIN_VALUE, Integer.MIN_VALUE, null, null)
    }

    fun setTitle(title: String, isOnly: Boolean) {
        if (isOnly)
            tvTitle!!.text = title
        else {
            setTitleAndHideDrawables(title)
        }
    }

    fun setTitleRight(title: String?) {
        tvTitleRight.text = title
        tvTitleRight.visibility = View.VISIBLE
        buttonRight.visibility = View.GONE
    }

    fun getRightTextView(): TextView? {
        return tvTitleRight
    }

    fun setOnLeftClickListener(listener: OnClickListener?) {
        buttonLeft.setOnClickListener(listener)
    }

    fun setOnRightClickListener(listener: OnClickListener?) {
        buttonRight.setOnClickListener(listener)
    }

    fun setOnTitleRightClickListener(listener: OnClickListener) {
        tvTitleRight.setOnClickListener(listener)
    }

    private fun setDrawableLeft(icon: Int) {
        if (icon == Integer.MIN_VALUE) {
            buttonLeft.visibility = View.INVISIBLE
        } else {
            buttonLeft.visibility = View.VISIBLE
            buttonLeft.setImageResource(icon)
        }
    }

    fun setDrawableRight(icon: Int) {
        if (icon == Integer.MIN_VALUE) {
            buttonRight.visibility = View.INVISIBLE
        } else {
            buttonRight.visibility = View.VISIBLE
            buttonRight.setImageResource(icon)
            tvTitleRight.visibility = View.GONE
        }
    }

    fun setBinding(title: String, drawableLeft: Int, leftListener: OnClickListener) {
        tvTitle!!.text = title
        setOnlyLeft(drawableLeft, leftListener)
    }

    fun setBinding(
        drawableLeft: Int, drawableRight: Int,
        leftListener: OnClickListener?, rightListener: OnClickListener?
    ) {

        setOnLeftClickListener(leftListener)
        setOnRightClickListener(rightListener)
        setDrawableLeft(drawableLeft)
        setDrawableRight(drawableRight)
    }

    fun setOnlyLeft(icon: Int, listener: OnClickListener) {
        setDrawableLeft(icon)
        setOnLeftClickListener(listener)
        setDrawableRight(Integer.MIN_VALUE)
        setOnRightClickListener(null)
    }


}
