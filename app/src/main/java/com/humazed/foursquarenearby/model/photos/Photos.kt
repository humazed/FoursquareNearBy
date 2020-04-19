package com.humazed.foursquarenearby.model.photos

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photos(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("dupesRemoved")
    val dupesRemoved: Int?,
    @SerializedName("items")
    val items: List<Item>?
) : Parcelable  