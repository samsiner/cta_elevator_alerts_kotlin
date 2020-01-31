package com.github.cta_elevator_alerts_kotlin.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository
import retrofit2.HttpException

class BuildStationsWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME = "BuildStationsWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AlertsRepository(database)

        return try {
            repository.buildAllStations()
            Result.success()
        } catch (exception: HttpException){
            Result.retry()
        }
    }
}