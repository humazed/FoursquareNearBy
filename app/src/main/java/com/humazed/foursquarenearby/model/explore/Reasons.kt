package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Reasons(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("items")
    val items: List<ItemX>?
) : Parcelable  