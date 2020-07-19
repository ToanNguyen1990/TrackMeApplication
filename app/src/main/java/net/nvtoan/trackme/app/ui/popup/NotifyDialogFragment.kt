package net.nvtoan.trackme.app.ui.popup

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_notify_dialog.*
import net.nvtoan.trackme.R
import net.toannt.hacore.ui.fragment.HMDialogFragment

class NotifyDialogFragment : HMDialogFragment() {

    private var message: String = ""
    private var onDismissCallback: (()-> Unit)? = null

    fun setMessage(message: String) {
        this.message = message
    }

    fun setDismissCallback(block: (()-> Unit)? = null) {
        this.onDismissCallback = block
    }

    override fun getFullScreen(): Boolean {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notify_dialog, container, false)
    }

    override fun initUI() {
        super.initUI()
        txtMessage.text = message
        btnOk.setOnClickListener {
            onDismissCallback?.invoke()
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onCancel(dialog)
    }
}