package com.github.cta_elevator_alerts

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.truth.content.IntentSubject.assertThat
import com.github.cta_elevator_alerts_kotlin.activities.DisplayAlertActivity
import com.github.cta_elevator_alerts_kotlin.activities.SpecificLineActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for SpecificLineActivity.
 */

@RunWith(AndroidJUnit4::class)
class InstrumentedTestSpecificLineActivity {
    @Rule
    var mActivityRule: IntentsTestRule<SpecificLineActivity> = object : IntentsTestRule<SpecificLineActivity>(
            SpecificLineActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val intent = Intent()
            intent.putExtra("line", "Red Line")
            intent.putExtra("fromFavorites", false)
            intent.putExtra("nickname", "Unknown")
            return intent
        }
    }

    @Test
    fun testToDisplayAlertFromSpecificLineActivity() {
        onView(withId(R.id.recycler_specific_line))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click()))
        val receivedIntent = Iterables.getOnlyElement(getIntents())
        assertThat(receivedIntent).hasComponentClass(DisplayAlertActivity::class.java)
    }
}