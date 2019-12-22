package com.github.cta_elevator_alerts.activities

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
import com.github.cta_elevator_alerts.adapters.StationListAdapter
import com.github.cta_elevator_alerts.model.Station
import com.github.cta_elevator_alerts.utils.NetworkWorker
import com.github.cta_elevator_alerts.viewmodels.MainViewModel

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
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var favoritesAdapter: StationListAdapter
    private lateinit var alertsAdapter: StationListAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvAlertsTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val about = findViewById<ImageView>(R.id.img_home_icon)
        about.setImageResource(R.drawable.icon_info)
        about.setOnClickListener{this.toAboutActivity(it)}

        val backArrow = findViewById<ImageView>(R.id.img_back_arrow)
        backArrow.visibility = View.INVISIBLE

        stationAlertsViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        //Create adapter to display favorites
        val favoritesRecyclerView = findViewById<RecyclerView>(R.id.recycler_favorite_stations)
        favoritesAdapter = StationListAdapter(this)
        favoritesRecyclerView.adapter = favoritesAdapter
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)

        //Create adapter to display alerts
        val alertsRecyclerView = findViewById<RecyclerView>(R.id.recycler_station_alerts)
        alertsAdapter = StationListAdapter(this)
        alertsRecyclerView.adapter = alertsAdapter
        alertsRecyclerView.layoutManager = LinearLayoutManager(this)

        //Create SharedPreferences for last updated date/time
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        tvAlertsTime = findViewById(R.id.txt_update_alert_time)
        val time = sharedPreferences.getString("LastUpdatedTime", "")
        if (time != null && time != "") tvAlertsTime.text = time

        //        addTestButtons();

        addSwipeRefresh()
        addAlertsObserver()
        addFavoritesObserver()
        addLastUpdatedObserver()
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

    private fun addAlertsObserver() {
        stationAlertsViewModel.stationAlerts.observe(this, Observer<List<Station>>{
            alertsAdapter.updateStationList(it)

            //If no alerts
            val tv = findViewById<TextView>(R.id.noStationAlerts)
            if (it.isEmpty()) {
                tv.visibility = View.VISIBLE
            } else {
                tv.visibility = View.GONE
            }
        }
        )
    }

    private fun addFavoritesObserver() {
        stationAlertsViewModel.favorites.observe(this, Observer<List<Station>>{
            favoritesAdapter.updateStationList(it)

            //If no favorites
            val tv = findViewById<TextView>(R.id.noFavoritesAdded)
            if (it.isEmpty()) {
                tv.visibility = View.VISIBLE
            } else {
                tv.visibility = View.GONE
            }
        })
    }

    private fun addLastUpdatedObserver() {
        stationAlertsViewModel.updateAlertsTime.observe(this, Observer<String>{
            tvAlertsTime.setText(it)

            val editor = sharedPreferences.edit()
            editor.putString("LastUpdatedTime", it)
            editor.apply()
        })
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
