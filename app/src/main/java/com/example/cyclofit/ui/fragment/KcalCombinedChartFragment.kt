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
import com.example.cyclofit.databinding.FragmentKcalCombinedChartBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.utils.Constants
import com.example.cyclofit.ui.utils.Constants.met
import com.example.cyclofit.ui.utils.Months
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class KcalCombinedChartFragment : Fragment() {
    lateinit var binding: FragmentKcalCombinedChartBinding
    private var userWeight = ""
    private lateinit var mFireStore: FirebaseFirestore
    private var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFireStore = FirebaseFirestore.getInstance()
        binding = FragmentKcalCombinedChartBinding.inflate(inflater, container, false)
        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.CYCLOFIT_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPreferences.getString("timer", null)
        val type: Type = object : TypeToken<ArrayList<Shared?>?>() {}.type

        //This is for accessing the user data form Firestore collection
        mFireStore.collection(Constants.USERS)
            // the document id to get the field of User.
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {
                userWeight = it.toObject(User::class.java)!!.weight.toDouble().toString()
                if (json != null) {
                    sp = gson.fromJson(json, type)
                }
                val combinedData = CombinedData().apply {
                    setData(generateLineData())
                    setData(generateBarData())
                    setValueTypeface(Typeface.DEFAULT)
                }
                setCombinedChart(combinedData)
            }
        return binding.root
    }

    private fun getCaloriesForLineChart(userWeight: String): MutableList<Entry> {
        val barArrayList = mutableListOf<Entry>()
        for ((x, i) in sp.withIndex()) {
            val y = i.time.toInt()
            val wt = userWeight.toFloat() // in kg
            var totalCalsBurnt: Float = y * (met * 3.5f * wt)
            totalCalsBurnt /= 200 * 1000
            barArrayList.add(BarEntry(x.toFloat(), totalCalsBurnt))
        }
        return barArrayList
    }

    private fun getCaloriesForBarChart(userWeight: String): MutableList<BarEntry> {
        val barArrayList = mutableListOf<BarEntry>()
        for ((x, i) in sp.withIndex()) {
            val y = i.time.toInt()
            val wt = userWeight.toFloat() // in kg
            var totalCalsBurnt: Float = y * (met * 3.5f * wt)
            totalCalsBurnt /= 200 * 1000
            barArrayList.add(BarEntry(x.toFloat(), totalCalsBurnt))
        }
        return barArrayList
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
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
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
                position = XAxisPosition.BOTTOM
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

    private fun generateLineData(): LineData {
        val lineData = LineData()
        val lineDataSet = LineDataSet(getCaloriesForLineChart(userWeight), "Line DataSet")
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

    private fun generateBarData(): BarData {
        val dataSet = BarDataSet(getCaloriesForBarChart(userWeight), "Bar")
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