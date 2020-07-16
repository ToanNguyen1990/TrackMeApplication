package net.toannt.hacore.utils.databinding

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object SpinnerViewBindingAdapter {

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    @JvmStatic
    fun captureSelectedValue(spinner: AppCompatSpinner): Any {
        return spinner.selectedItem
    }

    @BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
    @JvmStatic
    fun bindSpinnerData(
        spinner: AppCompatSpinner,
        newSelectedValue: Any?,
        newTextAttrChanged: InverseBindingListener
    ) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                newTextAttrChanged.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinner.setSelection(getIndexOfItem(spinner, newSelectedValue))
    }

    private fun getIndexOfItem(spinner: AppCompatSpinner, item: Any?): Int {
        val a = spinner.adapter

        if (item == null || a.count == 0) {
            return 0
        }

        for (i in 0 until a.count) {
            if (a.getItem(i) == item) {
                return i
            }
        }
        return 0
    }
}