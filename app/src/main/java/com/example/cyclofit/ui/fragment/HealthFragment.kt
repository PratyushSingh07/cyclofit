package com.example.cyclofit.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.cyclofit.`interface`.HeartApi
import com.example.cyclofit.databinding.FragmentHealthBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.ui.activities.GraphActivity
import com.example.cyclofit.ui.adapter.ViewAdapter
import com.example.cyclofit.ui.fragment.HomeFragment.Companion.timer
import com.example.cyclofit.ui.utils.Constants
import com.github.mikephil.charting.data.Entry
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

 class HealthFragment : BaseFragment() {

     lateinit var binding: FragmentHealthBinding
     var tabTitle  = arrayOf("Kcal","Time","Distance")
     var sp = ArrayList<Shared>()


     companion object{
         var Datalist=ArrayList<String>()
     }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHealthBinding.inflate(inflater, container, false)



        val pager = binding.viewPager
        pager.isSaveEnabled = false
        val tl = binding.tabs
        pager.adapter = ViewAdapter(childFragmentManager,lifecycle )

        TabLayoutMediator(tl,pager){ tab,position->
            tab.text = tabTitle[position]
        }.attach()

        binding.cvOfHeartRate.setOnClickListener{
            startActivity(Intent(context,GraphActivity::class.java))
        }
        return binding.root
    }
//    private fun fetchCalsBurnt(){
//        // Duration of physical activity in minutes × (MET × 3.5 × your weight in kg) / 200 =calories burned per minute
//        var durationOfActivity=0;
//        if (!timer.isEmpty())
//         durationOfActivity= timer.toInt()//in minutes
//        val MET=12 // for bicycles
//        val wt=60 // in kg
//        var totalCalsBurnt=0.0;
//          totalCalsBurnt=durationOfActivity*(MET*3.5*wt)
//        totalCalsBurnt/=200*1000;
//        binding.valueOfKcalsBurnt.setText(totalCalsBurnt.toString())
//    }
    private fun fetchHeartRate() {

        val httpClient = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        httpClient.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        val retrofit = Retrofit.Builder().baseUrl("https://api.thingspeak.com/channels/1951371/fields/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(HeartApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {

             val list = ArrayList<String>()
//            val list = ArrayList<String>()
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
//            for(i in list) println("Hiiiii $i") ->this is working fine
            Datalist=list
        }
    }

    private fun setText(value: String) {
        requireActivity().runOnUiThread {
            binding.valueOfHeartRate.text = value
            hideProgressDialog()
        }
    }

     override fun onResume() {
         super.onResume()
         showProgressDialog()
         fetchHeartRate()
         totalCal()
     }

     fun totalCal(){

         val sharedPreferences = requireContext().getSharedPreferences(
             Constants.CYCLOFIT_PREFERENCES,
             Context.MODE_PRIVATE)
         val gson = Gson()
         val json = sharedPreferences.getString("timer",null)

         val type: Type = object : TypeToken<ArrayList<Shared?>?>() {}.type

         if(json!=null) {
             sp = gson.fromJson(json, type)

             var x: Float = 0F
             for (i in sp) {
                 var y = i.time.toInt()
                 val MET = 12 // for bicycles
                 val wt = 60 // in kg
                 var totalCalsBurnt = 0.0;
                 totalCalsBurnt = y * (MET * 3.5 * wt)
                 totalCalsBurnt /= 200 * 1000;
                 x += totalCalsBurnt.toFloat()
             }
             binding.valueOfKcalsBurnt.text = String.format("%.2f", x).toDouble().toString()
         }
     }

}