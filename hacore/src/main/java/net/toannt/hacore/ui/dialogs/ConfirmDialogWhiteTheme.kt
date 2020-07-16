package net.toannt.hacore.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.StringRes
import com.homa.app.demo.hacore.R
import kotlinx.android.synthetic.main.dialog_confirm_core.*

open class ConfirmDialogWhiteTheme(context: Context) :
    CoreDialog(context) {

    private var ok: OnClickListener? = null
    private var cancel: OnClickListener? = null

    override val layout: Int = R.layout.dialog_confirm_white_theme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button_ok.setOnClickListener {
            ok?.onClick()
            dismiss()
        }
        button_cancel.setOnClickListener {
            cancel?.onClick()
            dismiss()
        }
    }

    fun show(
        title: String = context.getString(R.string.data_changed),
        message: String = context.getString(R.string.do_you_want_to_save),
        @StringRes confirm: Int = R.string.save,
        @StringRes cancelText: Int = R.string.no,
        ok: OnClickListener? = null,
        cancel: OnClickListener? = null
    ) {
        this.ok = ok
        this.cancel = cancel
        super.show()
        if (!TextUtils.isEmpty(title)) {
            text_title?.text = title
        }
        if (message.isNotEmpty()) {
            text_message?.text = message
        }
        button_cancel?.text = context.getString(cancelText)
        button_ok?.text = context.getString(confirm)
    }

    interface OnClickListener {
        fun onClick()
    }
}