package com.github.cta_elevator_alerts_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository

/**
 * ViewModel between MainActivity and StationRepository
 * to display station alerts
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AllAlertsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val alertsRepository = AlertsRepository(database)

    val allAlertStations = alertsRepository.alertStations
    val lastUpdatedTime = alertsRepository.lastUpdatedTime
}
