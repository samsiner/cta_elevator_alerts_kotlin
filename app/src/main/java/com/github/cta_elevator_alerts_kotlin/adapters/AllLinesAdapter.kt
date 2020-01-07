package com.github.cta_elevator_alerts_kotlin.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.databinding.FragmentAllLinesBinding
import com.github.cta_elevator_alerts_kotlin.viewmodels.AllLinesViewModel

/**
 * Adapter for AllLinesActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class AllLinesAdapter(private val context: Context, val mAllLinesViewModel: AllLinesViewModel, val binding: FragmentAllLinesBinding) : RecyclerView.Adapter<AllLinesAdapter.AllLinesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mLines = arrayOf("Red Line", "Blue Line", "Brown Line", "Green Line", "Orange Line", "Pink Line", "Purple Line", "Yellow Line")
//    private val toolbarTextView: TextView = (context as Activity).findViewById(R.id.txt_toolbar)

    inner class AllLinesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val allLinesTextView: TextView = itemView.findViewById(R.id.txt_all_lines)
        val trainIconImageView: ImageView = itemView.findViewById(R.id.img_train_icon)
        val alertIcon: ImageView = itemView.findViewById(R.id.img_alert_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllLinesViewHolder {
        val itemView = mInflater.inflate(R.layout.all_lines, parent, false)
        return AllLinesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllLinesViewHolder, position: Int) {
        val current = mLines[position]
        holder.allLinesTextView.text = current
        setTrainIcon(holder.trainIconImageView, current)

        if (mAllLinesViewModel!!.getAllLineAlerts(current)!!.isNotEmpty()) {
            holder.alertIcon.setImageResource(R.drawable.status_red)
        }

        (holder.allLinesTextView.parent as View).setOnClickListener {
//            val intent = Intent(context, SpecificLineActivity::class.java)
//            intent.putExtra("line", current)
//            intent.putExtra("fromFavorites", (context as Activity).intent.getBooleanExtra("fromFavorites", false))
//            intent.putExtra("nickname", context.intent.getStringExtra("nickname"))
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mLines.size
//    fun setToolbarTextView() = toolbarTextView.setText(R.string.all_lines)

    private fun setTrainIcon(imageView: ImageView, line: String) {
        when (line) {
            "Red Line" -> imageView.setImageResource(R.drawable.icon_redline)
            "Blue Line" -> imageView.setImageResource(R.drawable.icon_blueline)
            "Brown Line" -> imageView.setImageResource(R.drawable.icon_brownline)
            "Green Line" -> imageView.setImageResource(R.drawable.icon_greenline)
            "Orange Line" -> imageView.setImageResource(R.drawable.icon_orangeline)
            "Pink Line" -> imageView.setImageResource(R.drawable.icon_pinkline)
            "Purple Line" -> imageView.setImageResource(R.drawable.icon_purpleline)
            "Yellow Line" -> imageView.setImageResource(R.drawable.icon_yellowline)
            else -> Log.d("Train Icon", "Incorrect input from station lines array")
        }
    }
}