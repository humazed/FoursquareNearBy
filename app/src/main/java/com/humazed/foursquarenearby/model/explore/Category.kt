package com.humazed.foursquarenearby.model.explore

import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("icon")
    val icon: Icon?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pluralName")
    val pluralName: String?,
    @SerializedName("primary")
    val primary: Boolean?,
    @SerializedName("shortName")
    val shortName: String?
) : Parcelable  