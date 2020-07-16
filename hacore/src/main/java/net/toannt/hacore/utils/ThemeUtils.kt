package net.toannt.hacore.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import org.jetbrains.anko.displayMetrics

object ThemeUtils {

    /**
     * Get a resource id from a resource styled according to the the context's theme.
     */
    fun resolveResourceIdFromAttr(context: Context, @AttrRes attr: Int): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        val attributeResourceId = a.getResourceId(0, 0)
        a.recycle()
        return attributeResourceId
    }

    /**
     * Return a drawable object associated with a particular resource ID.
     *
     *
     *
     * Starting in [Build.VERSION_CODES.LOLLIPOP], the returned drawable will be styled for the
     * specified Context's theme.
     *
     * @param id The desired resource identifier, as generated by the aapt tool.
     * This integer encodes the package, type, and resource entry.
     * The value 0 is an invalid identifier.
     * @return Drawable An object that can be used to draw this resource.
     */
    fun getDrawable(context: Context, id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) context.getDrawable(id) else context.resources.getDrawable(
            id
        )
    }

    /**
     * Check if layout direction is RTL
     *
     * @param context the current context
     * @return `true` if the layout direction is right-to-left
     */
    fun isRtl(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    fun changeColorOfIconDrawableT(context: Context, drawable: Drawable?, color: Int) {
        try {
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(
                    ContextCompat
                        .getColor(context, color), PorterDuff.Mode.SRC_ATOP
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0)
            result = context.resources.getDimensionPixelSize(resourceId)
        return result
    }

    fun getScreenHeight(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.heightPixels
    }

    fun getScreenWidth(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.widthPixels
    }

    fun dpToPx(dp: Float, context: Context): Float {
        //Timber.d("Dp: $dp, density: ${context.resources.displayMetrics.density}, px: ${dp.times(context.resources.displayMetrics.density)}")
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

    fun dpToPx(dpRes: Int, context: Context): Float {
        val typedValue = TypedValue()
        context.resources.getValue(dpRes, typedValue, true)
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedValue.float, context.resources.displayMetrics)
    }

    fun spToPx(sp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
    }

    fun dpToSp(dp: Float, context: Context): Float {
        return dpToPx(dp, context) / context.resources.displayMetrics.scaledDensity
    }

    fun getDensity(context: Context) : Float {
        return context.displayMetrics.density
    }

}
