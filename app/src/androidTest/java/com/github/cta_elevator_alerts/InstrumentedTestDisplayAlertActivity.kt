package com.github.cta_elevator_alerts

import android.content.Intent

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.github.cta_elevator_alerts.activities.DisplayAlertActivity

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

/**
 * Instrumented tests for DisplayAlertActivity.
 */

@RunWith(AndroidJUnit4::class)
class InstrumentedTestDisplayAlertActivity {
    @Rule
    var mActivityRule: IntentsTestRule<DisplayAlertActivity> = object : IntentsTestRule<DisplayAlertActivity>(
            DisplayAlertActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val intent = Intent()
            intent.putExtra("stationID", "40900")
            return intent
        }
    }

    @Test
    fun test() {
        onView(withId(R.id.txt_toolbar)).check(matches(withText("Howard")))
    }
}