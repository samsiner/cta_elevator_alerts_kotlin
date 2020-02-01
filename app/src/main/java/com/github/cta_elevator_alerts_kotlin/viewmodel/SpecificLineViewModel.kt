package com.github.cta_elevator_alerts_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.github.cta_elevator_alerts_kotlin.database.getDatabase

import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.repository.AlertsRepository

/**
 * ViewModel for SpecificLineActivity 
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineViewModel(application: Application, lineName: String) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val alertsRepository = AlertsRepository(database)

    //TODO: Figure out Comparable or some way to do order
    //Sort list by station order
    val stationsByLine = Transformations.map(alertsRepository.stationsByLine(lineName)){
        it.sortedWith()

    })

    val allLineAlerts = alertsRepository.getAllLineAlerts(lineName)

    fun buildLines() {
        alertsRepository.buildLines()
    }
}

class SpecificLineViewModelFactory(
        private val application: Application, private val lineName: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpecificLineViewModel::class.java)) {
            return SpecificLineViewModel(application, lineName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}