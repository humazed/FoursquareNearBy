package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filter(
    @SerializedName("key")
    val key: String?,
    @SerializedName("name")
    val name: String?
) : Parcelable  