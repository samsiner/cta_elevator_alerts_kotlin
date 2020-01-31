package com.github.cta_elevator_alerts_kotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.databinding.AllLinesBinding
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentAllLinesBinding
import com.github.cta_elevator_alerts_kotlin.domain.Line
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

        val viewModel = ViewModelProvider(this).get(AllLinesViewModel::class.java)

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

class AllLinesAdapter(private val allLinesListener: LineListener): ListAdapter<Line, AllLinesAdapter.ViewHolder>(LineDiffCallback()) {

    class ViewHolder private constructor(val binding: AllLinesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Line, lineListener: LineListener) {
            binding.line = item
            binding.lineListener = lineListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflator = LayoutInflater.from(parent.context)
                val binding = AllLinesBinding.inflate(layoutInflator, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), allLinesListener)
    }
}

//    fun setToolbarTextView() = toolbarTextView.setText(R.string.all_lines)
//

class LineDiffCallback : DiffUtil.ItemCallback<Line>(){
    override fun areItemsTheSame(oldItem: Line, newItem: Line): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Line, newItem: Line): Boolean {
        return oldItem == newItem
    }
}

class LineListener(val clickListener: (name: String) -> Unit){
    fun onClick(line: Line) = clickListener(line.name)
}
