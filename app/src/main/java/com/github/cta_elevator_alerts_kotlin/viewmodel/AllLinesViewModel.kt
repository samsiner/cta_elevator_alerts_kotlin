package com.github.cta_elevator_alerts_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.cta_elevator_alerts_kotlin.database.getDatabase
import com.github.cta_elevator_alerts_kotlin.domain.Line

import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository

/**
 * ViewModel between SpecificLineActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AllLinesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val alertsRepository = AlertsRepository(database)

    val lines = alertsRepository.lines

//    val allLineAlerts: LiveData<List<Station>>
//        get() = repository!!.getAllLineAlerts(line)

//        fun getStationName(stationID: String): String? = repository!!.mGetStationName(stationID)
//    fun getHasElevator(stationID: String): Boolean = repository!!.mGetHasElevator(stationID)
//    fun getIsFavorite(stationID: String): Boolean = repository!!.isFavorite(stationID)
//    fun getHasElevatorAlert(stationID: String): Boolean = repository!!.mGetHasElevatorAlert(stationID)
}