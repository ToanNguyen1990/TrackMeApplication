package net.toannt.hacore.utils.databinding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ViewBindingAdapter {

    @BindingAdapter("viewBackground")
    @JvmStatic
    fun setBackgroundView(view : View, @DrawableRes res: Int) {
        var drawable: Drawable? = null
        if (res != 0) {
            drawable = view.context.getDrawable(res)
        }

        view.background = drawable
    }

    @BindingAdapter("viewBackground")
    @JvmStatic
    fun setBackgroundImageViewFromUrl(view : ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }
}