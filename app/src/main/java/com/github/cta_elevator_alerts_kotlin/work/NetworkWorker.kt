package com.github.cta_elevator_alerts_kotlin.work

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository
import java.util.*

/**
 * Worker to update data from CTA website.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class NetworkWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

//    private val alertsRepository: AlertsRepository? = AlertsRepository.getInstance(applicationContext as Application)

    override fun doWork(): Result {
        //Check for connection first
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork == null || !activeNetwork.isConnected) {
            Log.d("NetworkWorker", "Not connected")
//            alertsRepository!!.setConnectionStatus(false)
            return Result.failure()
        }

        try {
            Log.d("NetworkWorker", "Building Stations and Alerts")
//            val pastAlerts = alertsRepository!!.mGetStationAlertIDs() as ArrayList<String>
//            alertsRepository.buildStations()
//            alertsRepository.buildAlerts()
//            val currentAlerts = alertsRepository.mGetStationAlertIDs() as ArrayList<String>
//            NotificationPusher.createAlertNotifications(applicationContext, pastAlerts, currentAlerts)
        } catch (e: Exception) {
            Log.d("NetworkWorker", "Failed to build")
            e.printStackTrace()
            return Result.failure()
        }

        Log.d("NetworkWorker", "Succeeded in build")

        return Result.success()
    }
}
