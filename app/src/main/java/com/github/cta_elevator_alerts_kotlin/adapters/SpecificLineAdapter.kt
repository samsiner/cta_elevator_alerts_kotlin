package com.github.cta_elevator_alerts_kotlin.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.github.cta_elevator_alerts.R
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.activities.DisplayAlertActivity
import com.github.cta_elevator_alerts_kotlin.activities.SpecificLineActivity
import com.github.cta_elevator_alerts_kotlin.fragments.SpecificLineFragment

/**
 * Adapter for all stations with SpecificLineActivity (RecyclerView)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

class SpecificLineAdapter(private val context: Context, private val lineStations: List<String>?) : RecyclerView.Adapter<SpecificLineAdapter.SpecificLineAdapterViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val toolbar: androidx.appcompat.widget.Toolbar = (context as Activity).findViewById(R.id.toolbar)
    private val toolbarTextView: TextView = (context as Activity).findViewById(R.id.txt_toolbar)
    private val backArrow: ImageView = (context as Activity).findViewById(R.id.img_back_arrow)
    private val homeIcon: ImageView = (context as Activity).findViewById(R.id.img_home_icon)

    inner class SpecificLineAdapterViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        internal val specificLineTextView: TextView = itemView.findViewById(R.id.txt_line_station)
        internal val verticalBarTop: View = itemView.findViewById(R.id.view_vertical_bar_top)
        internal val verticalBarBottom: View = itemView.findViewById(R.id.view_vertical_bar_bottom)
        internal val circle: View = itemView.findViewById(R.id.view_circle)
        internal val circleDrawable: GradientDrawable = circle.background as GradientDrawable
        internal val adaImageView: ImageView = itemView.findViewById(R.id.img_ada)
        internal val statusImageView: ImageView = itemView.findViewById(R.id.img_status)
        internal val starIcon: ImageView = itemView.findViewById(R.id.img_star_icon)

        fun setUI(lineName: String, verticalBarTop: View, verticalBarBottom: View, circle: GradientDrawable) {
            when (lineName) {
                "Red Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorRedLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorRedLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorRedLine))
                }
                "Blue Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorBlueLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorBlueLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorBlueLine))
                }
                "Brown Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorBrownLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorBrownLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorBrownLine))
                }
                "Green Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorGreenLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorGreenLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorGreenLine))
                }
                "Orange Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorOrangeLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorOrangeLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorOrangeLine))
                }
                "Pink Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorPinkLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorPinkLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorPinkLine))
                }
                "Purple Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorPurpleLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorPurpleLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorPurpleLine))
                }
                "Yellow Line" -> {
                    verticalBarTop.setBackgroundResource(R.color.colorYellowLine)
                    verticalBarBottom.setBackgroundResource(R.color.colorYellowLine)
                    circle.setStroke(5, ContextCompat.getColor(context, R.color.colorYellowLine))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificLineAdapterViewHolder {
        val itemView = mInflater.inflate(R.layout.specific_line_station, parent, false)
        return SpecificLineAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpecificLineAdapterViewHolder, position: Int) {
        val currStationID = lineStations!![position]
        val currStationName = (context as SpecificLineFragment).getStationName(currStationID)
        val transparentColor = ContextCompat.getColor(context, R.color.colorTransparent)
        val hasElevator = context.getHasElevator(currStationID)
        val isFavorite = context.getIsFavorite(currStationID)

        holder.setUI(toolbarTextView.text.toString(), holder.verticalBarTop, holder.verticalBarBottom, holder.circleDrawable)
        if (position == 0) {
            holder.verticalBarTop.setBackgroundColor(transparentColor)
        }
        if (position == lineStations.size - 1) {
            holder.verticalBarBottom.setBackgroundColor(transparentColor)
        }

        holder.specificLineTextView.text = currStationName
        holder.adaImageView.visibility = View.VISIBLE
        if (!hasElevator) holder.adaImageView.visibility = View.INVISIBLE

        holder.statusImageView.visibility = View.VISIBLE
        holder.circle.visibility = View.VISIBLE
        if (!context.getHasElevatorAlert(currStationID)) {
            holder.statusImageView.visibility = View.INVISIBLE
        } else {
            holder.circle.visibility = View.INVISIBLE
        }

        if (isFavorite) {
            holder.starIcon.visibility = View.VISIBLE
        } else {
            holder.starIcon.visibility = View.INVISIBLE
        }

        (holder.specificLineTextView.parent as View).setOnClickListener {
            val intent = Intent(it.context, DisplayAlertActivity::class.java)
            intent.putExtra("stationID", currStationID)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lineStations?.size ?: 0
    }

    fun setToolbar(lineName: String) {
        toolbarTextView.text = lineName
        when (lineName) {
            "Red Line" -> toolbar.setBackgroundResource(R.color.colorRedLine)
            "Blue Line" -> toolbar.setBackgroundResource(R.color.colorBlueLine)
            "Brown Line" -> toolbar.setBackgroundResource(R.color.colorBrownLine)
            "Green Line" -> toolbar.setBackgroundResource(R.color.colorGreenLine)
            "Orange Line" -> toolbar.setBackgroundResource(R.color.colorOrangeLine)
            "Pink Line" -> toolbar.setBackgroundResource(R.color.colorPinkLine)
            "Purple Line" -> toolbar.setBackgroundResource(R.color.colorPurpleLine)
            "Yellow Line" -> {
                val colorID = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                toolbar.setBackgroundResource(R.color.colorYellowLine)
                toolbarTextView.setTextColor(colorID)
                backArrow.setColorFilter(colorID)
                homeIcon.setColorFilter(colorID)
            }
        }
    }
}
