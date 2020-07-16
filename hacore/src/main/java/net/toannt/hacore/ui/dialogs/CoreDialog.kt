package net.toannt.hacore.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.homa.app.demo.hacore.R
import net.toannt.hacore.utils.ThemeUtils

abstract class CoreDialog(context: Context) : Dialog(context, R.style.CoreTheme_Dialog) {

    protected var mOwnerActivity: Activity? = null

    protected val width: Int
        get() = ThemeUtils.getScreenWidth(context) * 9 / 10

    protected val height: Int
        get() = WindowManager.LayoutParams.WRAP_CONTENT

    protected var maxHeight: Int = Int.MIN_VALUE

    protected abstract val layout: Int

    protected val windowAnimations: Int
        get() = R.style.CoreTheme_Dialog_Anim_Fade

    init {
        mOwnerActivity = if (context is Activity) context else null
        if (mOwnerActivity != null)
            ownerActivity = mOwnerActivity!!
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mOwnerActivity = ownerActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layout)
        val window = window
        if (window != null) {
            val params = window.attributes
            params.windowAnimations = windowAnimations
//            params.width = width
//            params.height = if (maxHeight != Int.MIN_VALUE) maxHeight else height
            window.attributes = params
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}