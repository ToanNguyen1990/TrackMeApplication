package net.toannt.hacore.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import net.toannt.hacore.datasources.HMDataBindingSource

abstract class HMBindingActivity<T : ViewDataBinding>(@LayoutRes layoutResId: Int) : HMActivity(),
    HMDataBindingSource<T>, LifecycleOwner {

    override val dataBinding: T by lazy {
        DataBindingUtil.setContentView<T>(this@HMBindingActivity, layoutResId)
    }

    final override fun getLayoutResId(savedInstanceState: Bundle?): Int? {
        this@HMBindingActivity.dataBinding.lifecycleOwner = this@HMBindingActivity
        this@HMBindingActivity.onContentViewCreated(this@HMBindingActivity.dataBinding.root, savedInstanceState)
        return null
    }
}