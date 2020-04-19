package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class SuggestedFilters(
    @SerializedName("filters")
    val filters: List<Filter>?,
    @SerializedName("header")
    val header: String?
) : Parcelable  