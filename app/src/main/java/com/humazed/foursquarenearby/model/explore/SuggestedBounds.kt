package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class SuggestedBounds(
    @SerializedName("ne")
    val ne: Ne?,
    @SerializedName("sw")
    val sw: Sw?
) : Parcelable  