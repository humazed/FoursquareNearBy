package com.humazed.foursquarenearby.persestance

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.humazed.foursquarenearby.model.explore.Venue
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "venue")
data class VenueEntity(
    @PrimaryKey @ColumnInfo(name = "id") val venueId: String,
    val name: String,
    val category: String,
    val distance: String,
    val imageUrl: String
) : Parcelable {

    companion object {
        fun from(venue: Venue, imageUrl: String): VenueEntity {
            return VenueEntity(
                venue.id,
                venue.name ?: "_",
                venue.categories?.get(0)?.shortName ?: "_",
                venue.location?.distance?.toString() ?: "",
                imageUrl
            )
        }
    }
}