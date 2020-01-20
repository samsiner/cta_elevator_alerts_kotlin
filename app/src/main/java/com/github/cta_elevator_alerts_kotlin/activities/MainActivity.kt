package com.github.cta_elevator_alerts_kotlin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.utils.NetworkWorker
import com.github.cta_elevator_alerts_kotlin.viewmodels.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

/**
 * MainActivity displays favorite stations and their
 * elevator status, all current alerts,
 * the most updated date/time, and the privacy policy.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class MainActivity : AppCompatActivity() {

    lateinit var stationAlertsViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up bottom navigation
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.my_nav_host_fragment)
        bottomNav.setupWithNavController(navController)

        stationAlertsViewModel = MainViewModel(application)
        addConnectionStatusObserver()
        addPeriodicWorker()
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.my_nav_host_fragment).navigateUp()

    private fun addConnectionStatusObserver() {
        stationAlertsViewModel.connectionStatus.observe(this, Observer<Boolean>{
            if ((!it)) {
                val toast = Toast.makeText(this, "Not connected - please refresh!", Toast.LENGTH_SHORT)
                toast.show()
            }
            stationAlertsViewModel.setConnectionStatus(true)
        })
    }

    private fun addPeriodicWorker() {
        val request = PeriodicWorkRequest.Builder(NetworkWorker::class.java, 1, TimeUnit.HOURS)
                .addTag("PeriodicWork")
                .setConstraints(Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .build())
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("PeriodicWork", ExistingPeriodicWorkPolicy.KEEP, request)
    }



    //    private void addTestButtons(){
    //        Button b = new Button(this);
    //        b.setText("Remove alert King");
    //        LinearLayout l = findViewById(R.id.LinearLayout);
    //        b.setOnClickListener(v -> {
    //            ArrayList<String> past = (ArrayList<String>) vm.mGetStationAlertIDs();
    //            vm.removeAlertKing();
    //            ArrayList<String> curr = (ArrayList<String>) vm.mGetStationAlertIDs();
    //            NotificationPusher.createAlertNotifications(this, past, curr);
    //        });
    //        l.addView(b);
    //
    //        Button b1 = new Button(this);
    //        b1.setText("Add alert Howard");
    //        b1.setOnClickListener(v -> {
    //            ArrayList<String> past = (ArrayList<String>) vm.mGetStationAlertIDs();
    //            vm.addAlertHoward();
    //            ArrayList<String> curr = (ArrayList<String>) vm.mGetStationAlertIDs();
    //            NotificationPusher.createAlertNotifications(this, past, curr);
    //        });
    //        l.addView(b1);
}
