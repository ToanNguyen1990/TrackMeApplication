package net.toannt.hacore.vm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UIViewState(
    val isLoading: Boolean = false,
    val progress: Int = 0,
    val isTimeout: Boolean = false,
    var isError: String? = null,
    val isUnAuthorized: Boolean = false,
    val isSuccess: Boolean = false
) : Parcelable