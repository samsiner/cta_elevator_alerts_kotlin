package com.github.cta_elevator_alerts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.truth.content.IntentSubject.assertThat
import androidx.test.filters.LargeTest
import com.github.cta_elevator_alerts_kotlin.activities.AllLinesActivity
import com.github.cta_elevator_alerts_kotlin.activities.DisplayAlertActivity
import com.github.cta_elevator_alerts_kotlin.activities.MainActivity
import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts_kotlin.model.StationDao
import com.github.cta_elevator_alerts_kotlin.model.StationRepository
import com.github.cta_elevator_alerts_kotlin.model.StationRoomDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for MainActivity.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class InstrumentedTestMainActivity {
    private var stationDao: StationDao? = null
    private var db: StationRoomDatabase? = null
    private var repository: StationRepository? = null

    @Rule
    val mActivityRule = IntentsTestRule(
            MainActivity::class.java)

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, StationRoomDatabase::class.java).build()
        stationDao = db!!.dao
        repository = StationRepository.getInstance(mActivityRule.activity.application)

        //Create and add sample station
        val station = Station("40900")
        stationDao!!.insert(station)
        stationDao!!.updateName("40900", "Howard")
        stationDao!!.setRedTrue("40900")
        stationDao!!.setPurpleTrue("40900")
        stationDao!!.setYellowTrue("40900")
        stationDao!!.addFavorite("40900")
        stationDao!!.setHasElevator("40900")
        stationDao!!.setAlert("40900", "short description")
    }

    @Test
    fun testAllLinesButton() {
        onView(withId(R.id.btn_to_all_lines)).perform(click())
        val receivedIntent = Iterables.getOnlyElement(getIntents())
        assertThat(receivedIntent).hasComponentClass(AllLinesActivity::class.java)
    }

    @Test
    fun testDisplayAlertDetailsFromMainActivityAlert() {
        onView(withId(R.id.recycler_station_alerts))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click()))
        val receivedIntent = Iterables.getOnlyElement(getIntents())
        assertThat(receivedIntent).hasComponentClass(DisplayAlertActivity::class.java)
    }

    //Must add at least one favorite first
    @Test
    fun testDisplayAlertDetailsFromMainActivityFavorites() {
        onView(withId(R.id.recycler_favorite_stations))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click()))
        val receivedIntent = Iterables.getOnlyElement(getIntents())
        assertThat(receivedIntent).hasComponentClass(DisplayAlertActivity::class.java)
    }

    @Test
    fun checkDatabaseAndDao() {
        assertEquals(stationDao!!.getName("40900"), "Howard")
        assertTrue(stationDao!!.getHasElevator("40900"))
        assertTrue(stationDao!!.getRed("40900"))
        assertTrue(stationDao!!.getPurple("40900"))
        assertTrue(stationDao!!.getYellow("40900"))
    }

    @Test
    fun checkRepository() {
        assertTrue(repository!!.mGetAllRoutes("40900")[0])
        assertEquals(repository!!.mGetStationName("40900"), "Howard")
        assertTrue(repository!!.mGetHasElevator("40900"))
    }

    @After
    fun closeDb() {
        db!!.close()
    }
}