package com.humazed.foursquarenearby.model.photos

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    @SerializedName("prefix")
    val prefix: String?,
    @SerializedName("suffix")
    val suffix: String?
) : Parcelable  