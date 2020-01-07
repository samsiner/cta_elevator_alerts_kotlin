package com.github.cta_elevator_alerts_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.adapters.SpecificLineAdapter
import com.github.cta_elevator_alerts_kotlin.adapters.SpecificLineAlertsAdapter
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentSpecificLineBinding
import com.github.cta_elevator_alerts_kotlin.model.Station
import com.github.cta_elevator_alerts_kotlin.viewmodels.SpecificLineViewModel

/**
 * A simple [Fragment] subclass.
 */
class SpecificLineFragment : Fragment() {

    private lateinit var binding : FragmentSpecificLineBinding
    private lateinit var viewModel : SpecificLineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_specific_line,
                container,
                false
        )

        viewModel = ViewModelProviders.of(this).get(SpecificLineViewModel::class.java)
        binding.specificLineViewModel = viewModel
        binding.lifecycleOwner = this

        val lineAlertIDs = viewModel.allLineAlerts
        val lineAlertsAdapter = SpecificLineAlertsAdapter(activity!!, lineAlertIDs!!)
        val specificLineAdapter = SpecificLineAdapter(activity!!, viewModel.getLine())

        if (lineAlertIDs.isNotEmpty()) {
            val lineAlertsRecyclerView = binding.recyclerSpecificLineAlertStations
            lineAlertsRecyclerView.adapter = lineAlertsAdapter
            lineAlertsRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        } else {
            val alerts = binding.tvElevatorAlerts.parent as ViewGroup
            alerts.removeView(binding.tvAllStations)
            alerts.removeView(binding.recyclerSpecificLineAlertStations)
            alerts.removeView(binding.tvElevatorAlerts)
        }

        val specificLineRecyclerView = binding.recyclerSpecificLine
//        specificLineAdapter.setToolbar(intent.getStringExtra("line"))
        specificLineRecyclerView.adapter = specificLineAdapter
        specificLineRecyclerView.layoutManager = LinearLayoutManager(activity!!)

        viewModel.favorites.observe(this, Observer<List<Station>>{
            lineAlertsAdapter.notifyDataSetChanged()
            specificLineAdapter.notifyDataSetChanged()
        })

        return binding.root
    }
}
