package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemX(
    @SerializedName("reasonName")
    val reasonName: String?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("type")
    val type: String?
) : Parcelable  