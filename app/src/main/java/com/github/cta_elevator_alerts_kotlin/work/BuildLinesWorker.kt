package com.github.cta_elevator_alerts_kotlin.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository

class BuildLinesWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AlertsRepository(database)

        return try {
            repository.buildAllLines()
            Result.success()
        } catch (exception: Exception){
            Result.retry()
        }
    }
}