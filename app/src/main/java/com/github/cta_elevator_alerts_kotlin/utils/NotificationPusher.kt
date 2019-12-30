package com.github.cta_elevator_alerts_kotlin.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

import com.github.cta_elevator_alerts.R
import com.github.cta_elevator_alerts_kotlin.activities.DisplayAlertActivity
import com.github.cta_elevator_alerts_kotlin.model.StationRepository

import java.util.ArrayList

/**
 * Pushes notification when station goes up or down.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

object NotificationPusher {

    fun createAlertNotifications(context: Context, pastAlerts: ArrayList<String>, currAlerts: ArrayList<String>) {
        val repository = StationRepository.getInstance(context.applicationContext as Application)

        Log.d("NotificationPusher", "Past Alerts: $pastAlerts")
        Log.d("NotificationPusher", "Curr Alerts: $currAlerts")

        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelOut = NotificationChannel("out", "Elevators Out", NotificationManager.IMPORTANCE_HIGH)
            channelOut.enableVibration(true)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channelOut)

            //Create notification builder
            val builder = NotificationCompat.Builder(context, "out")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)

            //Elevators newly working
            for (s in pastAlerts) {
                if (!currAlerts.contains(s) && repository!!.isFavorite(s)) {
                    //Create notification tap action
                    val intent = Intent(context, DisplayAlertActivity::class.java)
                    intent.putExtra("stationID", s)

                    val stackBuilder = TaskStackBuilder.create(context)
                    stackBuilder.addNextIntentWithParentStack(intent)
                    val uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()
                    val resultPendingIntent = stackBuilder.getPendingIntent(uniqueInt, PendingIntent.FLAG_UPDATE_CURRENT)

                    builder.setContentIntent(resultPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSmallIcon(R.drawable.elevate_logo_small)
                            .setColor(ContextCompat.getColor(context, R.color.colorAndroidGreen))
                            .setContentTitle("Elevator is working again!")
                            .setContentText("Elevator at " + repository.mGetStationName(s) + " is working again!")

                    notificationManager.notify(Integer.parseInt(s), builder.build())
                }
            }

            val channelWork = NotificationChannel("working", "Elevators Working", NotificationManager.IMPORTANCE_HIGH)
            channelOut.enableVibration(true)
            notificationManager.createNotificationChannel(channelWork)

            //Create notification builder
            val builder2 = NotificationCompat.Builder(context, "working")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX)

            //Elevators newly out
            for (s2 in currAlerts) {
                if (!pastAlerts.contains(s2) && repository!!.isFavorite(s2)) {
                    //Create notification tap action
                    val intent2 = Intent(context, DisplayAlertActivity::class.java)
                    intent2.putExtra("stationID", s2)
                    val stackBuilder2 = TaskStackBuilder.create(context)
                    stackBuilder2.addNextIntentWithParentStack(intent2)

                    val uniqueInt2 = (System.currentTimeMillis() and 0xfffffff).toInt()
                    val resultPendingIntent2 = stackBuilder2.getPendingIntent(uniqueInt2, PendingIntent.FLAG_UPDATE_CURRENT)

                    builder2.setContentIntent(resultPendingIntent2)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSmallIcon(R.drawable.elevate_logo_small)
                            .setColor(ContextCompat.getColor(context, R.color.colorAndroidRed))
                            .setContentTitle("Elevator is down!")
                            .setContentText("Elevator at " + repository.mGetStationName(s2) + " is down!")

                    notificationManager.notify(Integer.parseInt(s2), builder2.build())
                }
            }
        }
    }
}
