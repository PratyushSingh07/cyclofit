package com.example.cyclofit.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cyclofit.databinding.FragmentKcalPeiChartBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.utils.ChartUtils
import com.example.cyclofit.ui.utils.Constants
import com.example.cyclofit.ui.utils.Constants.met
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class KcalPeiChartFragment : Fragment(), OnChartValueSelectedListener {
    lateinit var binding: FragmentKcalPeiChartBinding
    private var userWeight = ""
    private lateinit var mFireStore: FirebaseFirestore
    private var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFireStore = FirebaseFirestore.getInstance()
        binding = FragmentKcalPeiChartBinding.inflate(inflater, container, false)
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
                setPieChart(getCalories(userWeight))
            }
        return binding.root
    }

    private fun getCalories(userWeight: String): MutableList<PieEntry> {
        val barArrayList = mutableListOf<PieEntry>()
        for (i in sp) {
            val y = i.time.toInt()
            val wt = userWeight.toDouble() // in kg
            var totalCalsBurnt: Double = y * (met * 3.5 * wt)
            totalCalsBurnt /= 200 * 1000
            barArrayList.add(
                PieEntry(
                    totalCalsBurnt.toFloat(),
                    "${String.format("%.2f", totalCalsBurnt)} Kcal"
                )
            )
        }
        return barArrayList
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
            setUsePercentValues(true)
            isDrawHoleEnabled = false
            description.isEnabled = false

            // entry label text styling
            setEntryLabelColor(Color.TRANSPARENT)
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
            data = pieData
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