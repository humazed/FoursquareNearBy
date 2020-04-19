package com.humazed.foursquarenearby.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.humazed.foursquarenearby.persestance.AppDatabase
import com.humazed.foursquarenearby.persestance.VenueEntity
import com.humazed.foursquarenearby.persestance.VenuesDoa
import com.humazed.foursquarenearby.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VenueEntityTest {
    private lateinit var database: AppDatabase
    private lateinit var venueDoa: VenuesDoa
    private val venueA =
        VenueEntity("1", "A", "AA", "12.2", "19")
    private val venueB =
        VenueEntity("2", "B", "BB", "12.2", "19")
    private val venueC =
        VenueEntity("3", "C", "CC", "12.2", "19")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        venueDoa = database.venuesDoa()

        // Insert Venue in non-alphabetical order to test that results are sorted by name
        venueDoa.insertAll(listOf(venueA, venueB, venueC))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetAllVenues() {
        val venuesList: List<VenueEntity> = getValue(venueDoa.getAllVenues())
        assertThat(venuesList.size, equalTo(3))

        // Ensure VenueEntity list is sorted by name
        assertThat(venuesList[0], equalTo(venueA))
        assertThat(venuesList[1], equalTo(venueB))
        assertThat(venuesList[2], equalTo(venueC))
    }


    @Test
    fun testDetVenueEntity() {
        assertThat(getValue(venueDoa.getVenue(venueA.venueId)), equalTo(venueA))
    }
}