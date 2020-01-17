package com.github.cta_elevator_alerts_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.adapters.AllLinesAdapter
import com.github.cta_elevator_alerts_kotlin.adapters.LineListener
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentAllLinesBinding
import com.github.cta_elevator_alerts_kotlin.viewmodels.AllLinesViewModel

/**
 * A simple [Fragment] subclass.
 */
class AllLinesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        val binding: FragmentAllLinesBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_all_lines,
                container,
                false
        )

        val viewModel = ViewModelProviders.of(this).get(AllLinesViewModel::class.java)
        binding.lifecycleOwner = this

        //Create adapter to display all alerts
        val allLinesAdapter = AllLinesAdapter(LineListener { lineName ->
            findNavController().navigate(
                    AllLinesFragmentDirections.actionAllLinesFragmentToSpecificLineFragment(lineName)
            )
        })
        binding.recyclerAllLines.adapter = allLinesAdapter

        viewModel.lines.observe(viewLifecycleOwner, Observer {
            it?.let {
                allLinesAdapter.submitList(it)
            }
        })

//        linesAdapter.setToolbarTextView()

        return binding.root
    }
}
