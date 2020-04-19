package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    @SerializedName("reasons")
    val reasons: Reasons?,
    @SerializedName("referralId")
    val referralId: String?,
    @SerializedName("venue")
    val venue: Venue?
) : Parcelable  