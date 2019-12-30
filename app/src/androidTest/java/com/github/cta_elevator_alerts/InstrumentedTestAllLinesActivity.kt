package com.github.cta_elevator_alerts

import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.github.cta_elevator_alerts_kotlin.activities.AllLinesActivity
import com.github.cta_elevator_alerts_kotlin.activities.SpecificLineActivity

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.truth.content.IntentSubject.assertThat

/**
 * Instrumented tests for AllLinesActivity.
 */

@RunWith(AndroidJUnit4::class)
class InstrumentedTestAllLinesActivity {
    @Rule
    var mActivityRule = IntentsTestRule(
            AllLinesActivity::class.java)

    @Test
    fun testToSpecificLineFromAllLines() {
        onView(withId(R.id.recycler_all_lines))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click()))
        val receivedIntent = Iterables.getOnlyElement(getIntents())
        assertThat(receivedIntent).hasComponentClass(SpecificLineActivity::class.java)
    }
}