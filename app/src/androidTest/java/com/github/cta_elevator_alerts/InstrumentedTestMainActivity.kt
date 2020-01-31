package com.github.cta_elevator_alerts

import android.content.Context
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
import com.github.cta_elevator_alerts_kotlin.ui.MainActivity
import com.github.cta_elevator_alerts_kotlin.database.AlertsDao
import com.github.cta_elevator_alerts_kotlin.repository.Repository
import com.github.cta_elevator_alerts_kotlin.database.Station
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
    private var alertsDao: AlertsDao? = null
    private var db: com.github.cta_elevator_alerts_kotlin.database.Room? = null
    private var repository: Repository? = null

    @Rule
    val mActivityRule = IntentsTestRule(
            MainActivity::class.java)

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Room::class.java).build()
        alertsDao = db!!.dao
        repository = Repository.getInstance(mActivityRule.activity.application)

        //Create and add sample station
        val station = Station("40900")
        alertsDao!!.insert(station)
        alertsDao!!.updateName("40900", "Howard")
        alertsDao!!.setRedTrue("40900")
        alertsDao!!.setPurpleTrue("40900")
        alertsDao!!.setYellowTrue("40900")
        alertsDao!!.addFavorite("40900")
        alertsDao!!.setHasElevator("40900")
        alertsDao!!.setAlert("40900", "short description")
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
        assertEquals(alertsDao!!.getName("40900"), "Howard")
        assertTrue(alertsDao!!.getHasElevator("40900"))
        assertTrue(alertsDao!!.getRed("40900"))
        assertTrue(alertsDao!!.getPurple("40900"))
        assertTrue(alertsDao!!.getYellow("40900"))
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