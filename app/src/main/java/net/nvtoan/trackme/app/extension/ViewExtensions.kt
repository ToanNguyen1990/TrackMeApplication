package net.nvtoan.trackme.app.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.nvtoan.trackme.R
import net.toannt.hacore.ui.widget.GridSpacingItemDecoration
import net.toannt.hacore.utils.decoration.MyDividerItemDecoration

fun RecyclerView.initHorizontal(context: Context): RecyclerView {
    val itemDecoration = MyDividerItemDecoration(context)
    itemDecoration.setDrawable(context.getDrawable(R.drawable.bg_item_divider))
    var itemDecoratorIndex = 0
    while (itemDecoratorIndex < itemDecorationCount) {
        removeItemDecorationAt(itemDecoratorIndex)
        itemDecoratorIndex += 1
    }

    addItemDecoration(itemDecoration)
    invalidateItemDecorations()

    setHasFixedSize(true)
    isNestedScrollingEnabled = true
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    return this
}

fun RecyclerView.initHorizontalWithDecoration(context: Context, @DrawableRes resId: Int = R.drawable.bg_item_divider): RecyclerView {
    val itemDecoration = MyDividerItemDecoration(context)
    itemDecoration.setDrawable(context.getDrawable(resId))
    var itemDecoratorIndex = 0
    while (itemDecoratorIndex < itemDecorationCount) {
        removeItemDecorationAt(itemDecoratorIndex)
        itemDecoratorIndex += 1
    }

    addItemDecoration(itemDecoration)
    invalidateItemDecorations()

    setHasFixedSize(true)
    isNestedScrollingEnabled = true
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    return this
}

fun RecyclerView.init(context: Context): RecyclerView {
    setHasFixedSize(true)
    isNestedScrollingEnabled = false
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    return this
}

fun RecyclerView.initWithDecoration(
    context: Context,
    @DrawableRes resId: Int = R.drawable.bg_item_divider
): RecyclerView {
    addItemDecoration(ItemDecorator(context, resId))
    setHasFixedSize(true)
    isNestedScrollingEnabled = false
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    return this
}

fun RecyclerView.initWithDecoration(context: Context, drawable: Drawable): RecyclerView {
    val itemDecoration = MyDividerItemDecoration(context)
    itemDecoration.setDrawable(drawable)
    var itemDecoratorIndex = 0
    while (itemDecoratorIndex < itemDecorationCount) {
        removeItemDecorationAt(itemDecoratorIndex)
        itemDecoratorIndex += 1
    }

    addItemDecoration(itemDecoration)
    invalidateItemDecorations()

    setHasFixedSize(true)
    isNestedScrollingEnabled = false
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    return this
}

fun RecyclerView.visible(itemCount: Int, viewEmpty: View) {
    visibility = if (itemCount == 0) View.GONE else View.VISIBLE
    viewEmpty.visibility = if (itemCount == 0) View.VISIBLE else View.GONE
}


fun RecyclerView.initGridView(columns: Int, spacing: Int): RecyclerView {

    var itemDecoratorIndex = 0
    while (itemDecoratorIndex < itemDecorationCount) {
        removeItemDecorationAt(itemDecoratorIndex)
        itemDecoratorIndex += 1
    }

    setHasFixedSize(true)
    isNestedScrollingEnabled = true
    layoutManager = GridLayoutManager(this.context, columns)
    itemAnimator = DefaultItemAnimator()

    val decoration = GridSpacingItemDecoration(columns, spacing, false, 0)
    addItemDecoration(decoration)
    invalidateItemDecorations()
    return this
}
