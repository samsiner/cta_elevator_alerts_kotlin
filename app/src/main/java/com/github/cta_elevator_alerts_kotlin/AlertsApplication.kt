package com.github.cta_elevator_alerts_kotlin

import android.app.Application
import androidx.work.*
import com.github.cta_elevator_alerts_kotlin.work.BuildLinesWorker
import com.github.cta_elevator_alerts_kotlin.work.BuildStationsWorker
import com.github.cta_elevator_alerts_kotlin.work.RefreshAlertsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */
class AlertsApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            buildLinesStationsAndAlerts()
        }
    }

    private fun buildLinesStationsAndAlerts(){
        addLinesWorker()
        addStationsOneTimeWorker()
        addAlertsOneTimeWorker()
        addStationsPeriodicWorker()
        addAlertsPeriodicWorker()
    }

    //Build all lines immediately
    private fun addLinesWorker() {
        val oneTimeLinesRequest = OneTimeWorkRequest.Builder(BuildLinesWorker::class.java)
                .build()

        WorkManager.getInstance(this).enqueue(oneTimeLinesRequest)
    }

    //Build all stations immediately
    private fun addStationsOneTimeWorker() {
         val oneTimeStationRequest = OneTimeWorkRequest.Builder(BuildStationsWorker::class.java)
                .build()

        WorkManager.getInstance(this).enqueue(oneTimeStationRequest)
    }

    //Build all alerts immediately
    private fun addAlertsOneTimeWorker() {
        val oneTimeAlertRequest = OneTimeWorkRequest.Builder(RefreshAlertsWorker::class.java)
                .build()

        WorkManager.getInstance(this).enqueue(oneTimeAlertRequest)
    }

    //Build all stations about once per month
    private fun addStationsPeriodicWorker() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()

        val repeatingRequest = PeriodicWorkRequest.Builder(BuildStationsWorker::class.java, 30, TimeUnit.DAYS)
                .addTag("StationsPeriodicWork")
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                BuildStationsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }

    //Build all alerts once an hour
    private fun addAlertsPeriodicWorker() {
        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

        val repeatingRequest = PeriodicWorkRequest.Builder(RefreshAlertsWorker::class.java, 1, TimeUnit.HOURS)
                .addTag("AlertsPeriodicWork")
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "AlertsPeriodicWork",
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }
}
