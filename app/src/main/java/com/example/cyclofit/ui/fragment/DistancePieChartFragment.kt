package com.example.cyclofit.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cyclofit.databinding.FragmentDistancePieChartBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.ui.utils.ChartUtils
import com.example.cyclofit.ui.utils.Constants
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DistancePieChartFragment : Fragment(), OnChartValueSelectedListener {
    lateinit var binding: FragmentDistancePieChartBinding
    var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDistancePieChartBinding.inflate(inflater, container, false)
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
        val pieDataList = mutableListOf<PieEntry>()
        for ((x, i) in distanceArrayList.withIndex()) {
            pieDataList.add(PieEntry(i.toFloat(), "${i.toFloat()} km"))
        }
        setPieChart(pieDataList)
        return binding.root
    }

    private fun setPieChart(dataList: MutableList<PieEntry>) {
        val dataSet = PieDataSet(dataList, "My Graph")
        dataSet.apply {
            valueTextSize = 16f
            valueTextColor = Color.BLACK
            setDrawIcons(false)
            sliceSpace = 3f
            iconsOffset = MPPointF(0f, 40f)
            selectionShift = 5f
            colors = ChartUtils.colors
        }

        binding.pieChart.apply {
            isDrawHoleEnabled = false
            description.isEnabled = false
            setUsePercentValues(true)

            // entry label text styling
            setEntryLabelColor(Color.TRANSPARENT) // hide label text in Pie chart
            setEntryLabelTypeface(Typeface.DEFAULT)
            setEntryLabelTextSize(16f)

            dragDecelerationFrictionCoef = 0.95f
            rotationAngle = 0f
            // enable rotation of the chart by touch
            isRotationEnabled = true
            isHighlightPerTapEnabled = true

            animateY(1400, Easing.EaseInOutQuad)

            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                xEntrySpace = 7f
                yEntrySpace = 0f
                yOffset = 0f
            }
            val pieData = PieData(dataSet).apply {
                setValueFormatter(object : ValueFormatter() {
                    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                        return "${String.format("%.1f", value)} %"
                    }
                })
            }
            data = PieData(dataSet)
            highlightValues(null) // undo all highlights
            invalidate()
        }
        // add a selection listener
        binding.pieChart.setOnChartValueSelectedListener(this)
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return
    }

    override fun onNothingSelected() {
        return
    }
}