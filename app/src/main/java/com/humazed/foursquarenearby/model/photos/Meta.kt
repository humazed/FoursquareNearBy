package com.humazed.foursquarenearby.model.photos

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meta(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("requestId")
    val requestId: String?
) : Parcelable  