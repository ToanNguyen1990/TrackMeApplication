package net.toannt.hacore.datasources

import net.toannt.hacore.HMViewModel

interface HMViewModelSource<T : HMViewModel> {

    val viewModel: T
}