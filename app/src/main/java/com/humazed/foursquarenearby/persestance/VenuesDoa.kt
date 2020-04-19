package com.humazed.foursquarenearby.persestance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface VenuesDoa {
    @Query("SELECT * FROM venue ORDER BY name")
    fun getAllVenues(): LiveData<List<VenueEntity>>

    @Query("SELECT * FROM venue WHERE id = :venueId")
    fun getVenue(venueId: String): LiveData<VenueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(venues: List<VenueEntity>)

    @Query("DELETE FROM venue")
    fun deleteAllVenues()

}
