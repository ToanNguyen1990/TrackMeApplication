package net.toannt.hacore.ui.dialogs

import android.content.Context
import androidx.annotation.StringRes
import com.homa.app.demo.hacore.R
import kotlinx.android.synthetic.main.dialog_confirm_with_title.*

class ConfirmWithTitleDialog(context: Context) : CoreDialog(context) {

    override val layout: Int
        get() = R.layout.dialog_confirm_with_title

    fun show(
        text: String? = context.getString(R.string.logout_message),
        @StringRes buttonText: Int = R.string.dialog_ok,
        callback: (() -> Unit)? = null
    ) {
        super.show()
        text?.let { tv_message?.text = it }
        button?.setText(buttonText)
        button?.setOnClickListener {
            dismiss()
            callback?.invoke()
        }
    }
}