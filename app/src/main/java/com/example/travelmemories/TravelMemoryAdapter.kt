package com.example.travelmemories

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TravelMemoryAdapter(private val travelMemories: List<TravelMemory>) :
    RecyclerView.Adapter<TravelMemoryAdapter.TravelMemoryViewHolder>() {

    private val weatherDataMap: MutableMap<String, WeatherData> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelMemoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_travel_memory, parent, false)
        return TravelMemoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TravelMemoryViewHolder, position: Int) {
        val travelMemory = travelMemories[position]
        holder.bind(travelMemory)
    }

    override fun getItemCount(): Int = travelMemories.size

    fun setWeatherData(cityName: String, weatherData: WeatherData) {
        weatherDataMap[cityName] = weatherData
        notifyDataSetChanged()
    }

    inner class TravelMemoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val locationImage: ImageView = itemView.findViewById(R.id.locationImage)
        private val placeName: TextView = itemView.findViewById(R.id.placeName)
        private val placeLocation: TextView = itemView.findViewById(R.id.placeLocation)
        private val travelDate: TextView = itemView.findViewById(R.id.travelDate)
        private val weatherDescription: TextView = itemView.findViewById(R.id.weatherDescription)
        private val temperature: TextView = itemView.findViewById(R.id.temperature)

        fun bind(travelMemory: TravelMemory) {
            locationImage.setImageResource(travelMemory.locationImageResId)
            placeName.text = travelMemory.placeName
            placeLocation.text = travelMemory.placeLocation
            travelDate.text = travelMemory.travelDate

            val weatherData = weatherDataMap[travelMemory.placeName]
            weatherData?.let {
                val temperatureCelsius = convertKelvinToCelsius(it.main.temp)
                weatherDescription.text = it.weather.firstOrNull()?.description
                temperature.text = "$temperatureCelsius °C"
            }

            // Set click listener for opening location in Google Maps
            itemView.setOnClickListener {
                val location = Uri.encode(travelMemory.placeLocation)
                val intentUri = Uri.parse("geo:0,0?q=$location")
                val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                itemView.context.startActivity(mapIntent)
            }
        }
    }

    private fun convertKelvinToCelsius(kelvin: Double): Int {
        return (kelvin - 273.15).toInt()
    }
}
