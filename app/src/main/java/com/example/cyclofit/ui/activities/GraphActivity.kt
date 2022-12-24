package com.example.cyclofit.ui.activities




import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cyclofit.R
import com.example.cyclofit.ui.fragment.HealthFragment.Companion.Datalist
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class GraphActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.cyclofit.R.layout.activity_graph)
        supportActionBar?.hide()
//        val hf=HealthFragment()
//        if(Datalist.isEmpty()) println("List is empty") else println("List is not empty")
//        for(i in Datalist)println("Hello $i")
//        val barArrayList=mutableListOf<BarEntry>()
////        barArrayList.add(BarEntry(1f,10f))
////        barArrayList.add(BarEntry(2f,20f))
////        barArrayList.add(BarEntry(3f,30f))
////        barArrayList.add(BarEntry(4f,40f))
//        var x:Int=0
//        for(i in Datalist){
//            barArrayList.add(BarEntry(x*1f,i.toInt()*1f))
//            x++
//        }
//        val barChart=findViewById<BarChart>(R.id.barChart)
//        val barDataSet: BarDataSet = BarDataSet(barArrayList,"My Graph")
//        val baraData: BarData = BarData(barDataSet)
//        barChart.data=baraData
//        barDataSet.setColor(resources.getColor(R.color.purple_200))
//        barDataSet.valueTextColor= Color.BLACK
//        barDataSet.valueTextSize=16f
//        barChart.description.isEnabled=true

        var barArrayList=mutableListOf<Entry>()
        var x:Int=0
        for(i in Datalist){
            barArrayList.add(BarEntry(x*1f,i.toInt()*1f))
            x++
        }
        var lineChart=findViewById<LineChart>(com.example.cyclofit.R.id.reportingChart)
        var lineDataSet=LineDataSet(barArrayList,"My Graph")
        var lineData=LineData(lineDataSet)
        lineDataSet.mode=LineDataSet.Mode.CUBIC_BEZIER
        lineChart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        lineChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        lineChart.xAxis.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)

        lineChart.data=lineData
        lineDataSet.setColor(resources.getColor(R.color.purple_200))

        lineDataSet.setDrawFilled(true)
        lineDataSet.valueTextColor= Color.BLACK
        lineDataSet.valueTextSize=16f
        lineChart.description.isEnabled=true

        lineDataSet.setDrawFilled(true)
        val drawable = ContextCompat.getDrawable(this, R.drawable.gradient_3)
        lineDataSet.fillDrawable=drawable
    }
}