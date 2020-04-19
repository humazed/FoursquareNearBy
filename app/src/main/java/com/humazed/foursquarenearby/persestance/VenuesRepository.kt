package com.humazed.foursquarenearby.persestance

import com.humazed.foursquarenearby.model.explore.Venue
import humazed.github.com.kotlinandroidutils.d
import kotlin.concurrent.thread

class VenuesRepository private constructor(
    private val venuesDao: VenuesDoa
) {

    fun getAllVenues() = venuesDao.getAllVenues()

    fun saveVenues(venues: List<Pair<Venue, String>>) {
        d { "VenuesRepository.addVenues" }
        thread {
            venuesDao.deleteAllVenues()

            val venueEntities = venues.map { VenueEntity.from(it.first, it.second) }
            venuesDao.insertAll(venueEntities)
        }
    }


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: VenuesRepository? = null

        fun getInstance(gardenPlantingDao: VenuesDoa) =
            instance ?: synchronized(this) {
                instance ?: VenuesRepository(gardenPlantingDao).also { instance = it }
            }
    }
}