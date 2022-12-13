package com.example.cyclofit.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentKcalBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.ui.utils.Constants
import com.github.mikephil.charting.components.ComponentBase
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_kcal.*
import java.lang.reflect.Type


class KcalFragment : Fragment() {

    lateinit var binding: FragmentKcalBinding
    var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding= FragmentKcalBinding.inflate(inflater,container,false)
        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.CYCLOFIT_PREFERENCES,
            Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("timer",null)

        val type: Type = object : TypeToken<ArrayList<Shared?>?>() {}.type

        if(json!=null) {
            sp = gson.fromJson(json, type)
        }
        val barArrayList=mutableListOf<Entry>()
        var x:Int=0
        for(i in sp){
            var y=i.time.toInt()
            val MET=12 // for bicycles
            val wt=60 // in kg
            var totalCalsBurnt=0.0;
            totalCalsBurnt=y*(MET*3.5*wt)
            totalCalsBurnt/=200*1000;
            barArrayList.add(BarEntry(x*1f,totalCalsBurnt.toFloat()*1f))
            x++
        }
        val lineDataSet= LineDataSet(barArrayList,"My Graph")
        val lineData= LineData(lineDataSet)

        binding.kcalChart.data=lineData
        lineDataSet.color = getColor(resources,R.color.purple_200,resources.newTheme())
        lineDataSet.valueTextColor= Color.WHITE
        lineDataSet.valueTextSize=16f
        val axisLeft = binding.kcalChart.axisLeft.textColor
        binding.kcalChart.axisLeft
        lineDataSet.addColor(R.color.white)

        binding.kcalChart.description.isEnabled=true
        return binding.root
    }
}