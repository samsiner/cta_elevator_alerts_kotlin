package com.github.cta_elevator_alerts_kotlin.util

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.github.cta_elevator_alerts_kotlin.R
import com.github.cta_elevator_alerts_kotlin.domain.Line
import com.github.cta_elevator_alerts_kotlin.domain.Station

@BindingAdapter("stationName")
fun TextView.setStationName(item: Station?){
    item?.let{
        text = item.name
    }
}

@BindingAdapter("statusImage")
fun ImageView.setStatusImage(item: Station?){
    item?.let {
        visibility = when (item.hasElevatorAlert) {
            true -> VISIBLE
            else -> INVISIBLE
        }
    }
}

//TODO: figure this out
@BindingAdapter("statusLine")
fun ImageView.setLineImage(item: Line?){
//    item?.let {
//        visibility = when (item.hasElevatorAlert) {
//            true -> VISIBLE
//            else -> INVISIBLE
//        }
//    }
}

@BindingAdapter("lineIcon")
fun ImageView.setLineIcon(item: Line?){
    item?.let {
        when (item.name) {
            "Red Line" -> setImageResource(R.drawable.icon_redline)
            "Blue Line" -> setImageResource(R.drawable.icon_blueline)
            "Brown Line" -> setImageResource(R.drawable.icon_brownline)
            "Green Line" -> setImageResource(R.drawable.icon_greenline)
            "Orange Line" -> setImageResource(R.drawable.icon_orangeline)
            "Pink Line" -> setImageResource(R.drawable.icon_pinkline)
            "Purple Line" -> setImageResource(R.drawable.icon_purpleline)
            "Yellow Line" -> setImageResource(R.drawable.icon_yellowline)
        }
    }
}

@BindingAdapter("setColor")
fun View.setLineColor(lineName: String?){
    lineName?.let {
        when (lineName) {
            "Red Line" -> setBackgroundResource(R.color.colorRedLine)
            "Blue Line" -> setBackgroundResource(R.color.colorBlueLine)
            "Brown Line" -> setBackgroundResource(R.color.colorBrownLine)
            "Green Line" -> setBackgroundResource(R.color.colorGreenLine)
            "Orange Line" -> setBackgroundResource(R.color.colorOrangeLine)
            "Pink Line" -> setBackgroundResource(R.color.colorPinkLine)
            "Purple Line" -> setBackgroundResource(R.color.colorPurpleLine)
            "Yellow Line" -> setBackgroundResource(R.color.colorYellowLine)
        }
    }
}

@BindingAdapter("setCircleStroke")
fun View.setCircleStroke(lineName: String?){
    val circleDrawable: GradientDrawable = this.background as GradientDrawable

    lineName?.let {
        when (lineName) {
            "Red Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorRedLine))
            "Blue Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorBlueLine))
            "Brown Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorBrownLine))
            "Green Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorGreenLine))
            "Orange Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorOrangeLine))
            "Pink Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorPinkLine))
            "Purple Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorPurpleLine))
            "Yellow Line" -> circleDrawable.setStroke(5, ContextCompat.getColor(context, R.color.colorYellowLine))
        }
    }
}

@BindingAdapter("lineText")
fun TextView.setLineText(item: Line?){
    item?.let {
        text = item.name
    }
}

@BindingAdapter("topPosition")
fun View.setTop(topPosition: Int?){
    topPosition?.let {
        when (topPosition){
            0 -> setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent))
        }
    }
}

@BindingAdapter("bottomPosition", "totalPositions")
fun View.setBottom(bottomPosition: Int?, totalPositions: Int){
    bottomPosition?.let {
        when (bottomPosition){
            totalPositions - 1 -> setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent))
        }
    }
}

@BindingAdapter("favoriteImage")
fun ImageView.setFavoriteImage(item: Station?){
    item?.let {
        visibility = when (item.isFavorite) {
            true -> VISIBLE
            else -> INVISIBLE
        }
    }
}

@BindingAdapter("statusAda")
fun ImageView.setAdaStatus(item: Station?){
    item?.let {
        visibility = when (item.hasElevator) {
            true -> VISIBLE
            else -> INVISIBLE
        }
    }
}

@BindingAdapter("red")
fun View.setRed(item: Station?){
    item?.let {
        visibility = when (item.red) {
            true -> VISIBLE
            else -> GONE
        }
    }
}

@BindingAdapter("blue")
fun View.setBlue(item: Station?){
    item?.let {
        visibility = when (item.blue) {
            true -> VISIBLE
            else -> GONE
        }
    }
}

@BindingAdapter("brown")
fun View.setBrown(item: Station?){
    item?.let {
        visibility = when (item.brown) {
            true -> VISIBLE
            else -> GONE
        }
    }
}

@BindingAdapter("green")
fun View.setGreen(item: Station?){
    item?.let {
        visibility = when (item.green) {
            true -> VISIBLE
            else -> GONE
        }
    }
}

@BindingAdapter("orange")
fun View.setOrange(item: Station?){
    item?.let {
        visibility = when (item.orange) {
            true -> VISIBLE
            else -> GONE
        }
    }
}

@BindingAdapter("pink")
fun View.setPink(item: Station?){
    item?.let {
        visibility = when (item.pink) {
            true -> VISIBLE
            else -> GONE
        }
    }
}

@BindingAdapter("purple")
fun View.setPurple(item: Station?){
    item?.let {
        visibility = when (item.purple) {
            true -> VISIBLE
            else -> GONE
        }
    }
}

@BindingAdapter("yellow")
fun View.setYellow(item: Station?){
    item?.let {
        visibility = when (item.yellow) {
            true -> VISIBLE
            else -> GONE
        }
    }
}