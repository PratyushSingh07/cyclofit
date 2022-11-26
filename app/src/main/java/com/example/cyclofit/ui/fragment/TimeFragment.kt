package com.example.cyclofit.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentTimeBinding
import com.example.cyclofit.ui.fragment.HomeFragment.Companion.timeList
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class TimeFragment : Fragment() {
    lateinit var binding:FragmentTimeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTimeBinding.inflate(inflater,container,false)
        val barArrayList=mutableListOf<Entry>()
        var x:Int=0
        for(i in timeList){
            barArrayList.add(BarEntry(x*1f,i.toInt()*1f))
            x++
        }
//        val lineChart=findViewById<LineChart>(com.example.cyclofit.R.id.reportingChart)
        val lineDataSet= LineDataSet(barArrayList,"My Graph")
        val lineData= LineData(lineDataSet)
        binding.timeChart.data=lineData
        lineDataSet.setColor(resources.getColor(com.example.cyclofit.R.color.purple_200))
        lineDataSet.valueTextColor= Color.BLACK
        lineDataSet.valueTextSize=16f
        binding.timeChart.description.isEnabled=true
        return binding.root
    }
}