package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class LabeledLatLng(
    @SerializedName("label")
    val label: String?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lng")
    val lng: Double?
) : Parcelable  