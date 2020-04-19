package com.humazed.foursquarenearby.model.explore

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Venue(
    @SerializedName("categories")
    val categories: List<Category>?,
    @SerializedName("id")
    val id: String,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("photos")
    val photos: Photos?


) : Parcelable  