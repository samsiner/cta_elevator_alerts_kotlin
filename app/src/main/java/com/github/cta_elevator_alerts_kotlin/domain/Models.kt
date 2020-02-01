package com.github.cta_elevator_alerts_kotlin.domain

data class Station(
        val stationID: String,
        val hasElevator: Boolean,
        val hasElevatorAlert: Boolean,
        val isFavorite: Boolean,
        val red: Boolean,
        val blue: Boolean,
        val brown: Boolean,
        val green: Boolean,
        val orange: Boolean,
        val pink: Boolean,
        val purple: Boolean,
        val yellow: Boolean,
        val name: String,
        val alertDescription: String)

data class Line(
        val name: String,
        val hasElevatorAlert: Boolean,
        val stationIDs: List<String>)
