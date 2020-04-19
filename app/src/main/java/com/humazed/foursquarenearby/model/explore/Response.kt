package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(
    @SerializedName("groups")
    val groups: List<Group>?,
    @SerializedName("headerFullLocation")
    val headerFullLocation: String?,
    @SerializedName("headerLocation")
    val headerLocation: String?,
    @SerializedName("headerLocationGranularity")
    val headerLocationGranularity: String?,
    @SerializedName("suggestedBounds")
    val suggestedBounds: SuggestedBounds?,
    @SerializedName("suggestedFilters")
    val suggestedFilters: SuggestedFilters?,
    @SerializedName("totalResults")
    val totalResults: Int?
) : Parcelable  