package com.example.cyclofit.ui.activities

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyclofit.R
import com.example.cyclofit.ui.fragment.HealthFragment
import com.example.cyclofit.ui.fragment.HealthFragment.Companion.Datalist
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class GraphActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        supportActionBar?.hide()
        val hf=HealthFragment()
        if(Datalist.isEmpty()) println("List is empty") else println("List is not empty")
        for(i in Datalist)println("Hello $i")
        val barArrayList=mutableListOf<BarEntry>()
//        barArrayList.add(BarEntry(1f,10f))
//        barArrayList.add(BarEntry(2f,20f))
//        barArrayList.add(BarEntry(3f,30f))
//        barArrayList.add(BarEntry(4f,40f))
        var x:Int=0
        for(i in Datalist){
            barArrayList.add(BarEntry(x*1f,i.toInt()*1f))
            x++
        }
        val barChart=findViewById<BarChart>(R.id.barChart)
        val barDataSet: BarDataSet = BarDataSet(barArrayList,"My Graph")
        val baraData: BarData = BarData(barDataSet)
        barChart.data=baraData
        barDataSet.setColor(resources.getColor(R.color.purple_200))
        barDataSet.valueTextColor= Color.BLACK
        barDataSet.valueTextSize=16f
        barChart.description.isEnabled=true

    }
}