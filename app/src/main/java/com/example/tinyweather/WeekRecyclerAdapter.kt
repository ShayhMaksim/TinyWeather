package com.example.tinyweather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.StrictMath.round
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class WeekRecyclerAdapter(private val names: List<WeatherJSON.Daily>) :
    RecyclerView.Adapter<WeekRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDay: TextView? = null
        var textViewTemp: TextView? = null
        var imageViewDay: ImageView? = null

        init {
            textViewDay = itemView.findViewById(R.id.textViewDay)
            textViewTemp = itemView.findViewById(R.id.textViewTemp)
            imageViewDay = itemView.findViewById(R.id.imageViewDay)
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

        runBlocking {
            launch(Dispatchers.IO) {
                var image: Bitmap? = null
                try {
                    val `in` = URL("https://openweathermap.org/img/wn/${names[position].weather[0].icon}@2x.png").openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    holder.imageViewDay?.setImageBitmap(image)
                } catch (e: Exception) {
                    Log.e("Error Message", e.message.toString())
                    e.printStackTrace()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return names.size
    }
}