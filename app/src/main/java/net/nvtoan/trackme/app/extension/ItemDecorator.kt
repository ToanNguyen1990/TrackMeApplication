package net.nvtoan.trackme.app.extension

import android.content.Context
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import net.nvtoan.trackme.R

class ItemDecorator(
    context: Context,
    @DrawableRes private val resId: Int = R.drawable.bg_item_divider,
    orientation: Int = VERTICAL
) : DividerItemDecoration(context, orientation) {

    init {
        setDrawable(
            ContextCompat.getDrawable(
                context,
                resId
            )!!
        )
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + drawable!!.intrinsicHeight
            drawable!!.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            drawable!!.draw(canvas)
        }
    }
}