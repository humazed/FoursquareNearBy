package com.humazed.foursquarenearby

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.humazed.foursquarenearby.ui.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkVisibilityWhenOpen() {
        onView(withId(R.id.venuesRecyclerView)).check(matches(isDisplayed()))

        onView(withId(R.id.errorTv)).check(matches(not(isDisplayed())))

    }
}
