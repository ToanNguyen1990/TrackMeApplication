package net.toannt.hacore.ui.fragment

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable
import net.toannt.hacore.delegates.HMControllerDelegate
import timber.log.Timber

abstract class HMBottomSheetDialogFragment: BottomSheetDialogFragment(), HMControllerDelegate {

    open fun getFullScreen(): Boolean = false

    open fun getLayoutResId(savedInstanceState: Bundle?): Int? = null

    open fun getActionReceiverList(): List<String>? = null

    open fun getDataFromBroadcastReceiver(action: String, intent: Intent) = Unit

    override fun onContentViewCreated(parentView: View?, saveInstanceState: Bundle?) = Unit

    open fun getStyleId(): Int = DialogFragment.STYLE_NO_FRAME

    open fun getThemeStyle(): Int? = null

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getThemeStyle()?.let {
            setStyle(getStyleId(), it)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        bottomSheet?.fitsSystemWindows = false

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.findViewById<FrameLayout>(com.google.android.material.R.id.container)?.fitsSystemWindows = false
    }

    override fun onResume() {
        super.onResume()
        if (getFullScreen()) {
            setFullScreen()
        }

        this@HMBottomSheetDialogFragment.getActionReceiverList()?.let { actionList ->
            actionList.forEach { action ->
                intentFilter.addAction(action)
            }

            this@HMBottomSheetDialogFragment.activity?.registerReceiver(
                this@HMBottomSheetDialogFragment.actionReceiver,
                this@HMBottomSheetDialogFragment.intentFilter
            )
        }
    }

    override fun onPause() {
        super.onPause()
        this@HMBottomSheetDialogFragment.getActionReceiverList()?.let {
            this@HMBottomSheetDialogFragment.activity?.unregisterReceiver(this@HMBottomSheetDialogFragment.actionReceiver)
        }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.i("${this@HMBottomSheetDialogFragment::class.java.simpleName}  ----------- onCreateView")
        this@HMBottomSheetDialogFragment.getLayoutResId(savedInstanceState)?.let { layoutResId ->
            return inflater.inflate(layoutResId, container, false)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("${this@HMBottomSheetDialogFragment::class.java.simpleName}  ----------- onViewCreated")
        this@HMBottomSheetDialogFragment.onContentViewCreated(view, savedInstanceState)

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