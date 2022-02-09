package com.example.camping.util.data

import android.os.Parcelable
import com.example.camping.data.vo.Item
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListFragmentSafeArgs(
    val param : ParameterType,
    val value : String?,
    val extraValue : String?
) : Parcelable

@Parcelize
data class DetailFragmentSafeArgs(
    val item: Item
) : Parcelable

@Parcelize
data class SelectAreaFragmentSafeArgs(
    val param : ParameterType,
    val value: String
) : Parcelable