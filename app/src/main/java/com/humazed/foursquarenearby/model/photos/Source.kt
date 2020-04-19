package com.humazed.foursquarenearby.model.photos

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
) : Parcelable  