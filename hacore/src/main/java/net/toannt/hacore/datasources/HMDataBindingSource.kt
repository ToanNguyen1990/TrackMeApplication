package net.toannt.hacore.datasources

import androidx.databinding.ViewDataBinding

interface HMDataBindingSource<T : ViewDataBinding> {

    val dataBinding: T
}