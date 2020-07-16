package net.toannt.hacore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class HMBindingSwapLayoutFragment: HMFragment(){

    protected lateinit var dataBinding: ViewDataBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getLayoutResId(savedInstanceState)?.let {
            this@HMBindingSwapLayoutFragment.dataBinding = DataBindingUtil.inflate(inflater, it, container, false)
            this@HMBindingSwapLayoutFragment.dataBinding.lifecycleOwner = this@HMBindingSwapLayoutFragment
            return this@HMBindingSwapLayoutFragment.dataBinding.root
        }

        return null
    }

}