package com.github.cta_elevator_alerts_kotlin.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager

import com.github.cta_elevator_alerts.R
import com.github.cta_elevator_alerts_kotlin.adapters.StationListAdapter
import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts_kotlin.utils.NetworkWorker
import com.github.cta_elevator_alerts.viewmodels.MainViewModel
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.viewmodels.MainViewModel

import java.util.concurrent.TimeUnit

/**
 * MainActivity displays favorite stations and their
 * elevator status, all current alerts,
 * the most updated date/time, and the privacy policy.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class MainActivity : AppCompatActivity() {

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var stationAlertsViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addSwipeRefresh()
        addConnectionStatusObserver()
        addPeriodicWorker()
    }

    override fun onResume() {
        super.onResume()
        addOneTimeWorker()
    }

    private fun addSwipeRefresh() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_main_activity)
        mSwipeRefreshLayout.setOnRefreshListener {
            addOneTimeWorker()
            mSwipeRefreshLayout.isRefreshing = false
        }
    }

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

    private fun addOneTimeWorker() {
        val request = OneTimeWorkRequest.Builder(NetworkWorker::class.java)
                .addTag("OneTimeWork")
                .setConstraints(Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .build())
                .build()

        WorkManager.getInstance(this).enqueue(request)
    }

    fun toAllLinesActivity(v: View) {
        val intent = Intent(this@MainActivity, AllLinesActivity::class.java)
        startActivity(intent)
    }

    private fun toAboutActivity(v: View) {
        val intent = Intent(this@MainActivity, AboutActivity::class.java)
        startActivity(intent)
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
