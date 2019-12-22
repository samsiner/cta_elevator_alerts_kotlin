package com.github.cta_elevator_alerts.viewmodels

import android.app.Application

import androidx.lifecycle.AndroidViewModel

import com.github.cta_elevator_alerts.model.StationRepository

/**
 * ViewModel between DisplayAlertActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class DisplayAlertViewModel(application: Application) : AndroidViewModel(application) {
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
    val app = application

    fun setStationID(stationID: String) {
        val mRepository = StationRepository.getInstance(app)
        val arrList = mRepository!!.getAlertDetails(stationID)

        stationName = arrList[0]
        shortDesc = arrList[1]
        hasElevator = mRepository.mGetHasElevator(stationID)
        hasAlert = mRepository.mGetHasElevatorAlert(stationID)
        isFavorite = mRepository.isFavorite(stationID)
    }
}
