package com.example.cyclofit.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentDistanceBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.ui.utils.Constants
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DistanceFragment : Fragment() {

    lateinit var binding:FragmentDistanceBinding
    var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding=FragmentDistanceBinding.inflate(inflater,container,false)
        val sharedPreferences1 = requireContext().getSharedPreferences(
            Constants.CYCLOFIT_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPreferences1.getString("timer",null)
        val type: Type = object : TypeToken<ArrayList<Shared?>?>() {}.type

        if(json!=null){
            sp = gson.fromJson(json, type)
        }

        var distanceArrayList=ArrayList<String>()
        for(i in 0 until sp.size){
            distanceArrayList.add("0")
        }
        val barArrayList=mutableListOf<Entry>()
        var x:Int=0
        for(i in distanceArrayList){
            barArrayList.add(BarEntry(x*1f,i.toInt()*1f))
            x++
        }
//        val lineChart=findViewById<LineChart>(com.example.cyclofit.R.id.reportingChart)
        val lineDataSet= LineDataSet(barArrayList,"My Graph")
        val lineData= LineData(lineDataSet)
        binding.distanceChart.data=lineData
        lineDataSet.setColor(resources.getColor(com.example.cyclofit.R.color.purple_200))
        lineDataSet.valueTextColor= Color.BLACK
        lineDataSet.valueTextSize=16f
        binding.distanceChart.description.isEnabled=true
        lineDataSet.mode=LineDataSet.Mode.CUBIC_BEZIER
        binding.distanceChart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        binding.distanceChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        binding.distanceChart.xAxis.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        binding.distanceChart.setDrawGridBackground(false)
        binding.distanceChart.setDrawBorders(false)

        binding.distanceChart.data=lineData
        lineDataSet.setColor(resources.getColor(R.color.purple_200))

        lineDataSet.setDrawFilled(true)
        lineDataSet.valueTextColor= Color.BLACK
        lineDataSet.valueTextSize=16f
        binding.distanceChart.description.isEnabled=true

        lineDataSet.setDrawFilled(true)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_health)
        lineDataSet.fillDrawable=drawable

        return binding.root
    }

}