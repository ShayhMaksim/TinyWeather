package com.example.tinyweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.StrictMath.round
import java.text.SimpleDateFormat
import java.util.*

class WeekRecyclerAdapter(private val names: List<WeatherJSON.Daily>) :
    RecyclerView.Adapter<WeekRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDay: TextView? = null
        var textViewTemp: TextView? = null

        init {
            textViewDay = itemView.findViewById(R.id.textViewDay)
            textViewTemp = itemView.findViewById(R.id.textViewTemp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.week_recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sdfDay = SimpleDateFormat("dd")
        val sdfMonth = SimpleDateFormat("MM")
        val date = Date(names[position].dt * 1000)

        holder.textViewDay?.text = "${sdfDay.format(date)} ${monthNamesRussia[sdfMonth.format(date).toInt()]}\n"
        holder.textViewTemp?.text = "Температура на улице ${round(names[position].temp.day-273)}"
    }

    override fun getItemCount(): Int {
        return names.size
    }
}