package com.github.cta_elevator_alerts

import android.util.Log

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.TestDriver
import androidx.work.testing.WorkManagerTestInitHelper

import com.github.cta_elevator_alerts.activities.MainActivity
import com.github.cta_elevator_alerts.utils.NetworkWorker

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.concurrent.TimeUnit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

/**
 * Instrumented tests for Worker.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class InstrumentedTestWorker {

    @Rule
    val mActivityRule = IntentsTestRule(
            MainActivity::class.java)

    @Before
    fun setup() {
        val config = Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(SynchronousExecutor())
                .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(mActivityRule.activity, config)
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodicWorkAPIWorker() {
        val request = PeriodicWorkRequest.Builder(NetworkWorker::class.java, 15, TimeUnit.MINUTES)
                .addTag("Test")
                .build()

        val workManager = WorkManager.getInstance(mActivityRule.activity)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(mActivityRule.activity)

        workManager.enqueueUniquePeriodicWork("Test", ExistingPeriodicWorkPolicy.REPLACE, request).result.get()

        //Test that worker works even when activity is destroyed
        mActivityRule.finishActivity()
        assertNull(mActivityRule.activity)

        assert(testDriver != null)
        testDriver!!.setPeriodDelayMet(request.id)
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertEquals(workInfo.state, WorkInfo.State.ENQUEUED)
    }
}