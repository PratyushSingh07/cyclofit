package com.example.cyclofit.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentDistanceCombineChartBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.ui.utils.Constants
import com.example.cyclofit.ui.utils.Months
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DistanceCombineChartFragment : Fragment() {
    lateinit var binding: FragmentDistanceCombineChartBinding
    var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDistanceCombineChartBinding.inflate(inflater, container, false)
        val sharedPreferences1 = requireContext().getSharedPreferences(
            Constants.CYCLOFIT_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPreferences1.getString("timer", null)
        val type: Type = object : TypeToken<ArrayList<Shared?>?>() {}.type

        if (json != null) {
            sp = gson.fromJson(json, type)
        }

        val distanceArrayList = ArrayList<String>()
        for (i in 0 until sp.size) {
            distanceArrayList.add("0")
        }
        val lineDataList = mutableListOf<Entry>()
        val barDataList = mutableListOf<BarEntry>()
        for ((x, i) in distanceArrayList.withIndex()) {
            lineDataList.add(Entry(x.toFloat(), i.toFloat()))
            barDataList.add(BarEntry(x.toFloat(), i.toFloat()))
        }
        val combinedData = CombinedData().apply {
            setData(generateLineData(lineDataList))
            setData(generateBarData(barDataList))
            setValueTypeface(Typeface.DEFAULT)
        }
        setCombinedChart(combinedData)
        return binding.root
    }

    private fun setCombinedChart(combinedData: CombinedData) {
        binding.combinedChart.apply {
            data = combinedData
            invalidate()
            description.isEnabled = false
            setBackgroundColor(Color.TRANSPARENT)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            isHighlightFullBarEnabled = false
            // draw bars behind lines
            drawOrder = arrayOf(
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.BUBBLE,
                CombinedChart.DrawOrder.CANDLE,
                CombinedChart.DrawOrder.LINE,
                CombinedChart.DrawOrder.SCATTER
            )
            legend.apply {
                isWordWrapEnabled = true
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
            customizeYAxis(axisRight)
            customizeYAxis(axisLeft)
            val formatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val index = value.toInt() % Months.values().size
                    return Months.values()[index].name
                }
            }
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM
                axisMinimum = 0f - 0.25f
                granularity = 1f
                valueFormatter = formatter
                axisMaximum = combinedData.xMax + 0.25f
            }
        }
    }

    private fun customizeYAxis(axis: YAxis) {
        axis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(false)
            axisMinimum = 0f
        }
    }

    private fun generateLineData(dataList: MutableList<Entry>): LineData {
        val lineData = LineData()
        val lineDataSet = LineDataSet(dataList, "Line DataSet")
        lineDataSet.apply {
            color = resources.getColor(R.color.purple_200, resources.newTheme())
            valueTextSize = 12f
            valueTextColor = Color.BLACK
            setDrawValues(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            lineWidth = 2f
            axisDependency = YAxis.AxisDependency.LEFT
        }
        lineData.addDataSet(lineDataSet)
        return lineData
    }

    private fun generateBarData(dataList: MutableList<BarEntry>): BarData {
        val dataSet = BarDataSet(dataList, "Bar")
        dataSet.apply {
            color = ResourcesCompat.getColor(resources, R.color.light_green, resources.newTheme())
            valueTextColor = Color.TRANSPARENT
            dataSet.valueTextSize = 10f
            axisDependency = YAxis.AxisDependency.LEFT
        }
        val barWidth = 0.45f
        val barData = BarData(dataSet)
        barData.barWidth = barWidth
        return barData
    }
}