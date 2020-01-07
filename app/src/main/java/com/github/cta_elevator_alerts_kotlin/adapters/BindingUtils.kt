package com.github.cta_elevator_alerts_kotlin.adapters

import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.cta_elevator_alerts_kotlin.model.Station

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

@BindingAdapter("favoriteImage")
fun ImageView.setFavoriteImage(item: Station?){
    item?.let {
        visibility = when (item.isFavorite) {
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