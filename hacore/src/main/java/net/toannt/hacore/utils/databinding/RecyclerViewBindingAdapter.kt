package net.toannt.hacore.utils.databinding

import androidx.annotation.Nullable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.toannt.hacore.utils.layoutmanager.PreCachingLayoutManager

object RecyclerViewBindingAdapter {

    @BindingAdapter(value = ["recyclerViewGridLayoutAdapter", "recyclerViewGirdLayoutColumns"])
    @JvmStatic
    fun recyclerViewGridLayoutAdapter(recyclerView: RecyclerView?, adapter: RecyclerView.Adapter<*>?, columns: Int) {
        if (recyclerView == null || columns == 0 || adapter == null) {
            return
        }

        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, columns)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    @BindingAdapter(value = ["recyclerViewLinerLayoutAdapter", "recyclerViewLinearLayoutIsVertical"])
    @JvmStatic
    fun recyclerViewLayoutAdapter(
        recyclerView: RecyclerView, @Nullable adapter: RecyclerView.Adapter<*>?,
        isVertical: Boolean
    ) {
        if (adapter == null) {
            return
        }

        val layoutManager = LinearLayoutManager(recyclerView.context)
        layoutManager.orientation = if (isVertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    @BindingAdapter(value = ["recyclerViewLinerLayoutAdapter", "recyclerViewLinerLayoutCustomView", "recyclerViewLinearLayoutIsVertical"])
    @JvmStatic
    fun recyclerViewLayoutAdapter(
        recyclerView: RecyclerView, @Nullable adapter: RecyclerView.Adapter<*>?,
        isCustom: Boolean = false,
        isVertical: Boolean) {
        if (adapter == null) {
            return
        }

        val layoutManager = if (isCustom) PreCachingLayoutManager(recyclerView.context) else LinearLayoutManager(recyclerView.context)
        layoutManager.orientation = if (isVertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    @BindingAdapter("recyclerViewItemDecoration")
    @JvmStatic
    fun recyclerViewItemDecoration(recyclerView: RecyclerView, itemDecoration: RecyclerView.ItemDecoration) {
        var itemDecoratorIndex = 0
        while (itemDecoratorIndex < recyclerView.itemDecorationCount) {
            recyclerView.removeItemDecorationAt(itemDecoratorIndex)
            itemDecoratorIndex += 1
        }

        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.invalidateItemDecorations()
    }
}