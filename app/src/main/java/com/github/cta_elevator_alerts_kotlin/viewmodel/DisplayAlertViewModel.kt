package com.github.cta_elevator_alerts_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository

/**
 * ViewModel between DisplayAlertActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class DisplayAlertViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val alertsRepository = AlertsRepository(database)

    var stationID: String = ""
        private set
    var shortDesc: String = ""
        private set
    var stationName: String = ""
        private set
    var hasElevator: Boolean = false
        private set
    var hasAlert: Boolean = false
        private set
    var isFavorite: Boolean = false
        private set

    fun updateStationInfo(stationID: String) {
        val arrList = alertsRepository.getAlertDetails(stationID)

        this.stationID = stationID

        isFavorite = alertsRepository.isFavorite(stationID)
        stationName = arrList[0]
        shortDesc = arrList[1]
        hasElevator = alertsRepository.mGetHasElevator(stationID)
        hasAlert = alertsRepository.mGetHasElevatorAlert(stationID)
    }

    fun removeFavorite(){
        alertsRepository.removeFavorite(stationID)
    }

    fun addFavorite(){
        alertsRepository.addFavorite(stationID)
    }
}
