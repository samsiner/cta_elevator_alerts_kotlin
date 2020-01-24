package com.github.cta_elevator_alerts_kotlin.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.adapters.StationListAdapter
import com.github.cta_elevator_alerts_kotlin.adapters.StationListener
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentAllAlertsBinding
import com.github.cta_elevator_alerts_kotlin.utils.NetworkWorker
import com.github.cta_elevator_alerts_kotlin.viewmodels.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class AllAlertsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        val binding: FragmentAllAlertsBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_all_alerts,
                container,
                false
        )

        binding.swipeRefreshAll.setOnRefreshListener {
            addOneTimeWorker()
            binding.swipeRefreshAll.isRefreshing = false
        }

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        //Create adapter to display all alerts
        val alertsAdapter = StationListAdapter(StationListener { stationID ->
            findNavController().navigate(
                    AllAlertsFragmentDirections.actionAllAlertsFragmentToDisplayAlert(stationID)
            )
        })
        binding.recyclerStationAlerts.adapter = alertsAdapter

        //Create SharedPreferences for last updated date/time
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity!!)
        val tvAlertsTime = binding.txtUpdateAlertTime
        val time = sharedPreferences.getString("LastUpdatedTime", "")
        if (time != null && time != "") tvAlertsTime.text = time

        //        addTestButtons();

        viewModel.stationAlerts.observe(viewLifecycleOwner, Observer {
            it?.let {
                alertsAdapter.submitList(it)

                //If no alerts
                val tv = binding.noStationAlerts
                if (it.isEmpty()) {
                    tv.visibility = View.VISIBLE
                } else {
                    tv.visibility = View.GONE
                }
            }
        })

        viewModel.updateAlertsTime.observe(this, Observer<String>{
            tvAlertsTime.text = it

            val editor = sharedPreferences.edit()
            editor.putString("LastUpdatedTime", it)
            editor.apply()
        })

        addOneTimeWorker()
        return binding.root
    }

    private fun addOneTimeWorker() {
        val request = OneTimeWorkRequest.Builder(NetworkWorker::class.java)
                .addTag("OneTimeWork")
                .setConstraints(Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .build())
                .build()

        WorkManager.getInstance(activity!!).enqueue(request)
    }
}
