package com.humazed.foursquarenearby.model.photos

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    @SerializedName("checkin")
    val checkin: Checkin?,
    @SerializedName("createdAt")
    val createdAt: Int?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("prefix")
    val prefix: String?,
    @SerializedName("source")
    val source: Source?,
    @SerializedName("suffix")
    val suffix: String?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("visibility")
    val visibility: String?,
    @SerializedName("width")
    val width: Int?
) : Parcelable  