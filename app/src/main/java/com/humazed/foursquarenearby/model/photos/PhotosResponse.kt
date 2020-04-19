package com.humazed.foursquarenearby.model.photos

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotosResponse(
    @SerializedName("meta")
    val meta: Meta?,
    @SerializedName("response")
    val response: Response?
) : Parcelable  