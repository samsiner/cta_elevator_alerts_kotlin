package com.github.cta_elevator_alerts_kotlin.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.adapters.AllLinesAdapter
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentAllLinesBinding
import com.github.cta_elevator_alerts_kotlin.viewmodels.AllLinesViewModel

/**
 * A simple [Fragment] subclass.
 */
class AllLinesFragment : Fragment() {

    private lateinit var binding : FragmentAllLinesBinding
    private lateinit var viewModel : AllLinesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_all_lines,
                container,
                false
        )

        viewModel = ViewModelProviders.of(this).get(AllLinesViewModel::class.java)
        binding.allLinesViewModel = viewModel
        binding.lifecycleOwner = this

        val linesRecyclerView = binding.recyclerAllLines
        val linesAdapter = AllLinesAdapter(activity!!)
        linesAdapter.setToolbarTextView()
        linesRecyclerView.adapter = linesAdapter
        linesRecyclerView.layoutManager = LinearLayoutManager(activity!!)

//        val about = findViewById<ImageView>(R.id.img_home_icon)
//        about.visibility = View.INVISIBLE

        return binding.root
    }
}
