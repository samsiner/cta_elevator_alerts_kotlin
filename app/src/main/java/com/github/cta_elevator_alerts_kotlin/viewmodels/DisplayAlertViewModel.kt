package com.github.cta_elevator_alerts_kotlin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.cta_elevator_alerts_kotlin.repository.Repository

/**
 * ViewModel between DisplayAlertActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class DisplayAlertViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = Repository.getInstance(application)

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
        val arrList = mRepository!!.getAlertDetails(stationID)

        this.stationID = stationID

        isFavorite = mRepository.isFavorite(stationID)
        stationName = arrList[0]
        shortDesc = arrList[1]
        hasElevator = mRepository.mGetHasElevator(stationID)
        hasAlert = mRepository.mGetHasElevatorAlert(stationID)
    }

    fun removeFavorite(){
        mRepository!!.removeFavorite(stationID)
    }

    fun addFavorite(){
        mRepository!!.addFavorite(stationID)
    }
}
