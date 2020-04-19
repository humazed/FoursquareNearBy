package com.humazed.foursquarenearby.data

import com.humazed.foursquarenearby.persestance.VenueEntity
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CityWeatherTest {

    @Test
    fun test_default_values() {
        val defaultVenue = VenueEntity("1", "A", "AA", "12.2", "19")
        assertEquals("A", defaultVenue.name)
        assertEquals("12.2", defaultVenue.distance)
    }

}