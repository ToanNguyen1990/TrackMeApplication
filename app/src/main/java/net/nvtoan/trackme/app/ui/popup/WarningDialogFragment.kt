package net.nvtoan.trackme.app.ui.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_warning_dialog.*
import net.nvtoan.trackme.R
import net.toannt.hacore.ui.fragment.HMDialogFragment

class WarningDialogFragment: HMDialogFragment() {

    private var message: String = ""
    private var negativeMessage: String = "Ok"
    private var positiveMessage: String = "Cancel"
    private var onNegativeClicked: (()-> Unit)? = null
    private var onPositiveClicked: (()-> Unit)? = null

    fun setMessage(message: String) {
        this.message = message
    }

    fun setNegativeMessage(message: String) {
        this.negativeMessage = message
    }

    fun setPositiveMessage(message: String) {
        this.positiveMessage = message
    }

    fun setNegativeClicked(block: (() ->Unit)? = null) {
        onNegativeClicked = block
    }

    fun setPositiveClicked(block: (() ->Unit)? = null) {
        onPositiveClicked = block
    }

    override fun getFullScreen(): Boolean {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_warning_dialog, container, false)
    }

    override fun initUI() {
        super.initUI()
        txtMessage.text = message
        btnOk.text = positiveMessage
        btnCancel.text = negativeMessage
        btnOk.setOnClickListener {
            onPositiveClicked?.invoke()
            dismiss()
        }

        btnCancel.setOnClickListener {
            onNegativeClicked?.invoke()
            dismiss()
        }
    }
}