package com.github.cta_elevator_alerts_kotlin.viewmodels

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.repository.Repository

/**
 * ViewModel between MainActivity and StationRepository
 * to display station alerts
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: Repository? = Repository.getInstance(application)

    val stationAlerts: LiveData<List<Station>>
        get() = mRepository!!.mGetAllAlertStations()
    val favorites: LiveData<List<Station>>
        get() = mRepository!!.mGetAllFavorites()
    val updateAlertsTime: LiveData<String>
        get() = mRepository!!.updatedAlertsTime
    val connectionStatus: LiveData<Boolean>
        get() = mRepository!!.connectionStatus

    fun setConnectionStatus(b: Boolean) {
        mRepository!!.setConnectionStatus(b)
    }
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
