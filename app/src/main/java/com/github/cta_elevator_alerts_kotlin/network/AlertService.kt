package com.github.cta_elevator_alerts_kotlin.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * A retrofit service to fetch alerts from the CTA Alerts API.
 */
interface AlertService {
    @GET("alerts.aspx?outputType=JSON")
    fun getAllAlerts(): Deferred<NetworkStationContainer>
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 * Main entry point for network access. Call like `Network.devbytes.getPlaylist()`
 */
object AlertNetwork {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://lapi.transitchicago.com/api/1.0/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val alerts = retrofit.create(AlertService::class.java)
}
