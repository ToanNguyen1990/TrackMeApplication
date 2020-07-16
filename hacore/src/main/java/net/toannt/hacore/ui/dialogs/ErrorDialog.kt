package net.toannt.hacore.ui.dialogs

import android.content.Context
import com.homa.app.demo.hacore.R
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialog(context: Context) : CoreDialog(context) {
    override val layout: Int
        get() = R.layout.dialog_error

    fun show(text: String?, callback: (() -> Unit)? = null) {
        if (text.isNullOrEmpty()) return
        super.show()
        text_title?.text = text
        button_ok?.setOnClickListener {
            callback?.invoke()
            dismiss()
        }
    }
}