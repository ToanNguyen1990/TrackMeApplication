package net.toannt.hacore.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.homa.app.demo.hacore.R

class RatioImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    private var showRatioHeight : Boolean = true
    private var percentHeight : Float = 0.0f
    private var percentWidth: Float = 0.0f

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.RatioImageView, 0, 0).apply {
            try {
                percentWidth = getFloat(R.styleable.RatioImageView_percentWidth, 1.0f)
                percentHeight = getFloat(R.styleable.RatioImageView_percentHeight, 1.0f)
                showRatioHeight = getBoolean(R.styleable.RatioImageView_showPercentHeight, true)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when {
            showRatioHeight -> {
                val width = MeasureSpec.getSize(widthMeasureSpec)
                val height = (width * percentHeight).toInt()
                setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
            }
            else -> {
                val height = MeasureSpec.getSize(heightMeasureSpec)
                val width = (height * percentWidth).toInt()
                setMeasuredDimension( MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec)
            }
        }

    }
}