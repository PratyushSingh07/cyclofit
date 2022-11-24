package com.example.cyclofit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.`interface`.HeartApi
import com.example.cyclofit.databinding.FragmentHealthBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HealthFragment : Fragment() {

    lateinit var binding: FragmentHealthBinding
    private lateinit var mApiService: HeartApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHealthBinding.inflate(inflater, container, false)

        fetchHeartRate()

        return binding.root
    }

    private fun fetchHeartRate() {

        val httpClient = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        httpClient.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        val retrofit =
            Retrofit.Builder().baseUrl("https://api.thingspeak.com/channels/1951371/fields/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(HeartApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val list = ArrayList<String>()
            val averageHeart : String
            var total = 0

            val response = retrofit.getHeartRate()

            for(data in response.feeds){
                total += data.field1.toInt()
                list.add(data.field1)
            }
            averageHeart = (total/response.feeds.size).toString()
            setText(averageHeart)
            println(averageHeart)
        }
    }

    private fun setText(value: String) {
        requireActivity().runOnUiThread {
            binding.valueOfHeartRate.text = value
        }
    }

}