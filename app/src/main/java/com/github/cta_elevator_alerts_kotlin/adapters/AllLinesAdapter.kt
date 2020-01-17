package com.github.cta_elevator_alerts_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.databinding.AllLinesBinding
import com.github.cta_elevator_alerts_kotlin.model.Line

/**
 * Adapter for AllLinesActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

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