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

class FavoriteAlertsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val alertsRepository = AlertsRepository(database)

    val favoriteStations = alertsRepository.favoriteStations
    val lastUpdatedTime = alertsRepository.lastUpdatedTime

//    val stationAlerts: LiveData<List<Station>>
//        get() = mAlertsRepository!!.mGetAllAlertStations()
//    val favorites: LiveData<List<Station>>
//        get() = mAlertsRepository!!.mGetAllFavorites()
//    val updateAlertsTime: LiveData<String>
//        get() = mAlertsRepository!!.updatedAlertsTime
//    val connectionStatus: LiveData<Boolean>
//        get() = mAlertsRepository!!.connectionStatus
//
//    fun setConnectionStatus(b: Boolean) {
//        mAlertsRepository!!.setConnectionStatus(b)
//    }
//
//    fun getAllRoutes(stationID: String): BooleanArray {
//        return mRepository!!.mGetAllRoutes(stationID)
//    }
//
//    fun getHasElevatorAlert(stationID: String): Boolean {
//        return mRepository!!.mGetHasElevatorAlert(stationID)
//    }

//    fun removeAlertKing() {
//        mRepository!!.removeAlertKing()
//    }
//
//    fun addAlertHoward() {
//        mRepository!!.addAlertHoward()
//    }
//
//    fun mGetStationAlertIDs(): List<String> {
//        return mRepository!!.mGetStationAlertIDs()
//    }
}
