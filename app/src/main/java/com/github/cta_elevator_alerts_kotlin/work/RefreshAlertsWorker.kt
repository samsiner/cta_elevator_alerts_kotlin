package com.github.cta_elevator_alerts_kotlin.work

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository
import retrofit2.HttpException

class RefreshAlertsWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME = "RefreshAlertsWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AlertsRepository(database)

        return try {
            val beforeAlerts = repository.getAllAlertStationIDs()
//            repository.refreshAllAlerts()
//            repository.deleteAllAlerts()
//            repository.addAllAlerts()
            repository.buildAllAlerts()
            val afterAlerts = repository.getAllAlertStationIDs()
            Log.d("Database Alerts Worker", afterAlerts.size.toString())
            sendPushNotifications(beforeAlerts, afterAlerts)
            Result.success()
        } catch (exception: HttpException){
            //TODO: Fix toast for not connected
            val toast = Toast.makeText(applicationContext, "Not connected - please refresh!", Toast.LENGTH_SHORT)
            toast.show()
            Result.retry()
        }
    }

    private fun sendPushNotifications(beforeAlerts: List<String>, afterAlerts: List<String>){
        //TODO: Push notification logic
    }


}