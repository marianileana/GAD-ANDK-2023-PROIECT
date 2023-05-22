package com.example.travelmemories.ui.home

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelmemories.R
import com.example.travelmemories.TravelMemory
import com.example.travelmemories.TravelMemoryAdapter
import com.example.travelmemories.WeatherData
import com.example.travelmemories.WeatherService
import com.example.travelmemories.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val API_KEY = "cf0b737da98d2d6c6625e86e9e58caf1"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelMemoriesRecyclerView: RecyclerView
    private lateinit var travelMemoryAdapter: TravelMemoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        travelMemoriesRecyclerView = binding.travelMemoriesRecyclerView
        travelMemoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        travelMemoryAdapter = TravelMemoryAdapter(getSampleTravelMemories())
        travelMemoriesRecyclerView.adapter = travelMemoryAdapter

        for (memory in getSampleTravelMemories()) {
            getWeatherData(memory.placeName)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSampleTravelMemories(): List<TravelMemory> {
        val travelMemories = mutableListOf<TravelMemory>()

        travelMemories.add(
            TravelMemory(
                "Iași",
                "Palatul Culturii",
                "14.02.2023",
                R.drawable.iasi
            ))
        travelMemories.add(
            TravelMemory(
                "București",
                "Palatul Parlamentului",
                "07.03.203",
                R.drawable.bucuresti
            ))
        travelMemories.add(
            TravelMemory(
                "Timișoara",
                "Piata Operei",
                "15.04.2023",
                R.drawable.timisoara
            ))
        travelMemories.add(
            TravelMemory(
                "Pitești",
                "Centru",
                "15.05.2023",
                R.drawable.pitesti
            ))

        return travelMemories
    }

    private fun getWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getWeather(cityName, API_KEY)

        call.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        travelMemoryAdapter.setWeatherData(cityName, weatherData)
                    }
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                if (t is IOException) {
                    Log.e(TAG, "Network error: ${t.message}")
                    showErrorMessage("Network error occurred. Please check your internet connection.")
                } else if (t is HttpException) {
                    val errorCode = t.code()
                    Log.e(TAG, "HTTP error: $errorCode")
                    when (errorCode) {
                        404 -> showErrorMessage("Requested resource not found.")
                        500 -> showErrorMessage("Server error occurred.")
                        else -> showErrorMessage("An error occurred. Please try again later.")
                    }
                } else {
                    Log.e(TAG, "Error: ${t.message}")
                    showErrorMessage("An error occurred. Please try again later.")
                }
            }

            private fun showErrorMessage(message: String) {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Error")
                alertDialogBuilder.setMessage(message)
                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }

        })
    }
}