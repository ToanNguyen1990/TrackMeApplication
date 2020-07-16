package net.toannt.hacore.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.annotation.StringRes
import com.homa.app.demo.hacore.BuildConfig
import com.homa.app.demo.hacore.R
import kotlinx.android.synthetic.main.dialog_confirm_core.button_cancel
import kotlinx.android.synthetic.main.dialog_confirm_core.button_ok
import kotlinx.android.synthetic.main.dialog_confirm_core.text_title
import kotlinx.android.synthetic.main.dialog_input_core.*

open class InputDialog(context: Context, isMobile: Boolean = false) : CoreDialog(context) {

    private var ok: OnClickListener? = null
    private var cancel: OnClickListener? = null

    override val layout: Int =
        if (isMobile) R.layout.dialog_input_core_mobile else R.layout.dialog_input_core

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button_ok.setOnClickListener {
            ok?.onClick(edit_text.text.toString())
            dismiss()
        }
        button_cancel.setOnClickListener {
            cancel?.onClick(edit_text.text.toString())
            dismiss()
        }
    }

    fun show(
        title: String = context.getString(R.string.channel),
        type: Int = InputType.TYPE_CLASS_NUMBER,
        @StringRes confirm: Int = R.string.dialog_ok,
        @StringRes cancelText: Int = R.string.cancel,
        ok: OnClickListener? = null,
        cancel: OnClickListener? = null
    ) {
        this.ok = ok
        this.cancel = cancel
        super.show()
        if (!TextUtils.isEmpty(title)) {
            text_title?.text = title
        }
        edit_text.inputType = type
        button_cancel?.text = context.getString(cancelText)
        button_ok?.text = context.getString(confirm)
    }

    interface OnClickListener {
        fun onClick(text: String)
    }
}