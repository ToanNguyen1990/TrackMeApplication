package net.toannt.hacore.utils.databinding

import android.widget.EditText
import androidx.databinding.InverseBindingAdapter


object EditTextViewBindingAdapter {

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun getText(view: EditText): String {
        val text = view.text.toString()
        view.setSelection(text.length)
        return text
    }
}