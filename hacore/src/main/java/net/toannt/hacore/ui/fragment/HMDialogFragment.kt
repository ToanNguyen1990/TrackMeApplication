package net.toannt.hacore.ui.fragment

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import io.reactivex.disposables.CompositeDisposable
import java.util.*

abstract class HMDialogFragment : AppCompatDialogFragment() {

    open fun getBackgroundResource(): Int? = null

    open fun getFullScreen(): Boolean = false


    open fun initUI() = Unit

    open fun registerObservables() = Unit

    open fun getActionReceiverList(): List<String>? = null

    open fun getDataFromBroadcastReceiver(action: String, intent: Intent) = Unit

    open fun getStyleAnimation(): Int? = null

    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val intentFilter = IntentFilter()

    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (getActionReceiverList().isNullOrEmpty()) return

            intent?.action?.let {
                if (getActionReceiverList()!!.contains(it)) {
                    getDataFromBroadcastReceiver(it, intent)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // safety check
        if (dialog == null) {
            return
        }

        // set the animations to use on showing and hiding the dialog
        getStyleAnimation()?.let {
            dialog!!.window.setWindowAnimations(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (getFullScreen()) {
            setFullScreen()
        }
        this@HMDialogFragment.getActionReceiverList()?.let { actionList ->
            actionList.forEach { action ->
                intentFilter.addAction(action)
            }

            this@HMDialogFragment.activity?.registerReceiver(
                this@HMDialogFragment.actionReceiver,
                this@HMDialogFragment.intentFilter
            )
        }
    }

    override fun onPause() {
        super.onPause()
        this@HMDialogFragment.getActionReceiverList()?.let {
            this@HMDialogFragment.activity?.unregisterReceiver(this@HMDialogFragment.actionReceiver)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (getFullScreen()) {
            val window = Objects.requireNonNull(dialog.window)
            window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            getBackgroundResource()?.let {
                window?.setBackgroundDrawable(resources.getDrawable(it))
            }
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            window?.attributes = params
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    protected fun setFullScreen() {
        val decorView: View? = dialog?.window?.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView?.systemUiVisibility = uiOptions
    }

    protected fun setHideNavigation() {
        val decorView: View? = dialog?.window?.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView?.systemUiVisibility = uiOptions
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        registerObservables()
        if (getFullScreen()) {
            dialog?.window?.decorView?.setOnSystemUiVisibilityChangeListener {
                setFullScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

}