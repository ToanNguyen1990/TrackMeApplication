package net.toannt.hacore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import net.toannt.hacore.datasources.HMDataBindingSource

abstract class HMBindingDialogFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    HMDialogFragment(), HMDataBindingSource<T> {

    override lateinit var dataBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this@HMBindingDialogFragment.dataBinding =
            DataBindingUtil.inflate(inflater, this@HMBindingDialogFragment.layoutResId, container, false)
        this@HMBindingDialogFragment.dataBinding.lifecycleOwner = viewLifecycleOwner
        return this@HMBindingDialogFragment.dataBinding.root
    }
}