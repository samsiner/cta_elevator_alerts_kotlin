package com.github.cta_elevator_alerts_kotlin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.cta_elevator_alerts_kotlin.domain.Line

import com.github.cta_elevator_alerts_kotlin.repository.Repository

/**
 * ViewModel between SpecificLineActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AllLinesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository? = Repository.getInstance(application)

    val lines: LiveData<List<Line>>
        get() = repository!!.getAllLines()

//    val allLineAlerts: LiveData<List<Station>>
//        get() = repository!!.getAllLineAlerts(line)

//        fun getStationName(stationID: String): String? = repository!!.mGetStationName(stationID)
//    fun getHasElevator(stationID: String): Boolean = repository!!.mGetHasElevator(stationID)
//    fun getIsFavorite(stationID: String): Boolean = repository!!.isFavorite(stationID)
//    fun getHasElevatorAlert(stationID: String): Boolean = repository!!.mGetHasElevatorAlert(stationID)
}