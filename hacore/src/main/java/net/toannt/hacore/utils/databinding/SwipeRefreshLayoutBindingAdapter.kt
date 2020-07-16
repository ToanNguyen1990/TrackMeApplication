package net.toannt.hacore.utils.databinding

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object SwipeRefreshLayoutBindingAdapter {

    @BindingAdapter("swipeRefreshLayoutSetRefreshing")
    @JvmStatic
    fun swipeRefreshLayoutSetRefreshing(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }

    @BindingAdapter("swipeRefreshLayoutOnRefresh")
    @JvmStatic
    fun swipeRefreshLayoutOnRefresh(
        swipeRefreshLayout: SwipeRefreshLayout,
        listener: SwipeRefreshLayout.OnRefreshListener
    ) {
        swipeRefreshLayout.setOnRefreshListener(listener)
    }

    @BindingAdapter("swipeRefreshLayoutTintColor")
    @JvmStatic
    fun swipeRefreshLayoutTintColor(swipeRefreshLayout: SwipeRefreshLayout, @ColorInt colorTint: Int) {
        swipeRefreshLayout.setColorSchemeColors(colorTint)
    }

}