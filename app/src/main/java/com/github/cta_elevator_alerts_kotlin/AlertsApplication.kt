package com.github.cta_elevator_alerts_kotlin

import android.app.Application
import androidx.work.*
import com.github.cta_elevator_alerts_kotlin.work.NetworkWorker
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
        delayedInit()
    }

    private fun delayedInit(){
        applicationScope.launch {
//            addOneTimeWorker()
            addPeriodicWorker()
        }
    }

    private fun addPeriodicWorker() {
        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

        val repeatingRequest = PeriodicWorkRequest.Builder(NetworkWorker::class.java, 1, TimeUnit.HOURS)
                .addTag("PeriodicWork")
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "PeriodicWork",
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }
}
