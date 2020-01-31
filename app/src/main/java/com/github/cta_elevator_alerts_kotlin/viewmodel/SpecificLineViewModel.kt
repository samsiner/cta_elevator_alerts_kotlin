package com.github.cta_elevator_alerts_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.cta_elevator_alerts_kotlin.database.getDatabase

import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository

/**
 * ViewModel between SpecificLineActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val alertsRepository = AlertsRepository(database)

    //TODO: Figure out a different way to do this
    var lineName: String = ""
        set(value) {
            field = value
            lineStations = alertsRepository.getStationsByLine(value)
        }
    lateinit var lineStations: MutableLiveData<MutableList<Station>>

    val allLineAlerts = alertsRepository.getAllLineAlerts(lineName)

    fun buildLines() {
        alertsRepository.buildLines()
    }
}