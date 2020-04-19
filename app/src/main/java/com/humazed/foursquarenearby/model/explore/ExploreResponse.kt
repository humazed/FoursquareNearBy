package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExploreResponse(
    @SerializedName("meta")
    val meta: Meta?,
    @SerializedName("response")
    val response: Response?
) : Parcelable  