package net.toannt.hacore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import net.toannt.hacore.datasources.HMDataBindingSource

abstract class HMBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : HMFragment(),
    HMDataBindingSource<T> {

    private var realBinding: T? = null
    override val dataBinding: T
        get() = realBinding ?: throw IllegalStateException("Trying to access the binding outside of the view lifecycle.")

    override fun getLayoutResId(savedInstanceState: Bundle?): Int? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DataBindingUtil.inflate<T>(inflater, this@HMBindingFragment.layoutResId, container, false).also {
            realBinding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataBinding.lifecycleOwner = viewLifecycleOwner
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        realBinding = null
    }

}