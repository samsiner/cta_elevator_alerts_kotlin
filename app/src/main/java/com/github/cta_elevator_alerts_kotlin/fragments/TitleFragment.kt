package com.github.cta_elevator_alerts_kotlin.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.adapters.StationListAdapter
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentTitleBinding
import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts_kotlin.utils.NetworkWorker
import com.github.cta_elevator_alerts_kotlin.viewmodels.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_title,
                container,
                false
        )

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

//        val about = binding.
//        about.setImageResource(R.drawable.icon_info)
//        about.setOnClickListener{this.toAboutActivity(it)}

//        val backArrow = findViewById<ImageView>(R.id.img_back_arrow)
//        backArrow.visibility = View.INVISIBLE

        //Create adapter to display favorites
        val favoritesAdapter = StationListAdapter()
        binding.recyclerFavoriteStations.adapter = favoritesAdapter

        //Create adapter to display alerts
        val alertsAdapter = StationListAdapter()
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

        viewModel.favorites.observe(viewLifecycleOwner, Observer {
            it?.let {
                favoritesAdapter.submitList(it)

                //If no alerts
                val tv = binding.noFavoritesAdded
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

        binding.btnToAllLines.setOnClickListener {
            findNavController().navigate(
                    TitleFragmentDirections
                            .actionTitleFragmentToAllLinesFragment())
        }

        return binding.root
    }

}
