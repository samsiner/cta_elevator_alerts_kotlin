package com.github.cta_elevator_alerts_kotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentAllAlertsBinding
import com.github.cta_elevator_alerts_kotlin.databinding.IndividualStationBinding
import com.github.cta_elevator_alerts_kotlin.domain.Station
import com.github.cta_elevator_alerts_kotlin.viewmodel.AllAlertsViewModel
import com.github.cta_elevator_alerts_kotlin.work.RefreshAlertsWorker

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

        val viewModel = ViewModelProvider(this).get(AllAlertsViewModel::class.java)

        binding.lifecycleOwner = this

        //Create adapter to display all alerts
        val alertsAdapter = StationListAdapter(StationListener { stationID ->
            findNavController().navigate(
                    AllAlertsFragmentDirections.actionAllAlertsFragmentToDisplayAlertFragment(stationID)
            )
        })
        binding.recyclerStationAlerts.adapter = alertsAdapter

        viewModel.allAlertStations.observe(viewLifecycleOwner, Observer {
            it?.let {
                alertsAdapter.submitList(it)

                //If no alerts
                val tv = binding.noStationAlerts
                when (it.isEmpty()) {
                    true -> tv.visibility = View.VISIBLE
                    false -> tv.visibility = View.GONE
                }
            }
        })

        viewModel.lastUpdatedTime.observe(viewLifecycleOwner, Observer {
            Log.d("Time Gotten", it ?: "null")
            it?.let {
                binding.txtUpdateAlertTime.text = it
            }
        })

        return binding.root
    }

    private fun addOneTimeWorker() {
        val oneTimeAlertRequest = OneTimeWorkRequest.Builder(RefreshAlertsWorker::class.java)
                .build()

        WorkManager.getInstance(this.context!!).enqueue(oneTimeAlertRequest)
    }
}

class StationListAdapter(private val stationListener: StationListener): ListAdapter<Station, StationListAdapter.ViewHolder>(StationDiffCallback()) {

    class ViewHolder private constructor(val binding: IndividualStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Station, stationListener: StationListener){
            binding.station = item
            binding.stationListener = stationListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
                val binding = IndividualStationBinding.inflate(layoutInflator, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), stationListener)
    }
}

class StationDiffCallback : DiffUtil.ItemCallback<Station>(){
    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem.stationID == newItem.stationID
    }

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem == newItem
    }
}

class StationListener(val clickListener: (stationID: String) -> Unit){
    fun onClick(station: Station) = clickListener(station.stationID)
}

