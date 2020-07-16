package net.toannt.hacore.utils.databinding

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.databinding.BindingAdapter

object ViewAnimationBindingAdapter {

    @BindingAdapter("animatedFromResource", "isLoadAnimation")
    @JvmStatic
    fun setBackgroundView(view : View, @AnimRes res: Int, isLoadAnimation: Boolean = false) {
        val animation = AnimationUtils.loadAnimation(view.context, res)
        view.animation = animation
        when(isLoadAnimation) {
            true -> {
                view.startAnimation(animation)
            }
            else -> {
                view.clearAnimation()
            }
        }

    }
}