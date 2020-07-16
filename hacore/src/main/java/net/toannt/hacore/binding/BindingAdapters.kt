package net.toannt.hacore.binding

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:visibleGone")
    fun visibleGone(view: View, visible: Boolean) {
        view.isVisible = visible
    }
}
