package com.humazed.foursquarenearby.model.photos

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Checkin(
    @SerializedName("createdAt")
    val createdAt: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("timeZoneOffset")
    val timeZoneOffset: Int?,
    @SerializedName("type")
    val type: String?
) : Parcelable  