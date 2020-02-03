package com.github.cta_elevator_alerts_kotlin.work

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
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
            repository.refreshAllAlerts()
            Result.success()
        } catch (exception: HttpException){
            val toast = Toast.makeText(applicationContext, "Not connected - please refresh!", Toast.LENGTH_SHORT)
            toast.show()
            Result.retry()
        }
    }
}