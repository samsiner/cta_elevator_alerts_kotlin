package com.github.cta_elevator_alerts_kotlin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts_kotlin.model.Repository

/**
 * ViewModel between SpecificLineActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineViewModel(application: Application) : AndroidViewModel(application) {

    var line: String = ""
        set(value) {
            lineStations = repository?.getStationsByLine(value) ?: MutableLiveData(mutableListOf())
        }
    private val repository: Repository? = Repository.getInstance(application)
    lateinit var lineStations: MutableLiveData<MutableList<Station>>

    val favorites: LiveData<List<Station>>
        get() = repository!!.mGetAllFavorites()

    val allLineAlerts: LiveData<List<Station>>
        get() = repository!!.getAllLineAlerts(line)

//        fun getStationName(stationID: String): String? = repository!!.mGetStationName(stationID)
//    fun getHasElevator(stationID: String): Boolean = repository!!.mGetHasElevator(stationID)
//    fun getIsFavorite(stationID: String): Boolean = repository!!.isFavorite(stationID)
//    fun getHasElevatorAlert(stationID: String): Boolean = repository!!.mGetHasElevatorAlert(stationID)
}