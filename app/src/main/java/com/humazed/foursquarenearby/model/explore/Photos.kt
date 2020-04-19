package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photos(
    @SerializedName("count")
    val count: Int?
//    @SerializedName("groups")
//    val groups: List<Any>?
) : Parcelable  