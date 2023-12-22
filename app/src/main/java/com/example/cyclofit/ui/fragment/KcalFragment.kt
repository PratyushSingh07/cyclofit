package com.example.cyclofit.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentKcalBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.utils.Constants
import com.example.cyclofit.ui.utils.Constants.met
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class KcalFragment : Fragment() {
    lateinit var binding: FragmentKcalBinding
    private var userWeight = ""
    private lateinit var mFireStore: FirebaseFirestore
    private var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFireStore = FirebaseFirestore.getInstance()
        binding = FragmentKcalBinding.inflate(inflater, container, false)
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
                setLineChart()
            }
        return binding.root
    }

    private fun getCalories(userWeight: String): MutableList<Entry> {
        val barArrayList = mutableListOf<Entry>()
        for ((x, i) in sp.withIndex()) {
            val y = i.time.toInt()
            val wt = userWeight.toDouble() // in kg
            var totalCalsBurnt = 0.0;
            totalCalsBurnt = y * (met * 3.5 * wt)
            totalCalsBurnt /= 200 * 1000;
            barArrayList.add(BarEntry(x * 1f, totalCalsBurnt.toFloat() * 1f))
        }
        return barArrayList
    }

    private fun setLineChart() {
        //here the user weight is passed in the getCalories and after calculation the chart is shown
        val lineDataSet = LineDataSet(getCalories(userWeight), "My Graph")
        lineDataSet.color = getColor(resources, R.color.purple_200, resources.newTheme())
        lineDataSet.valueTextSize = 16f
        lineDataSet.addColor(R.color.white)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.setDrawFilled(true)
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.gradient_health)
        lineDataSet.fillDrawable = drawable
        val lineData = LineData(lineDataSet)

        binding.kcalChart.apply {
            data = lineData
            description.isEnabled = false
            invalidate()
            setDrawGridBackground(false)
            setDrawBorders(false)
            axisRight.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                setDrawAxisLine(false)
            }
            axisLeft.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                setDrawAxisLine(false)
            }
            xAxis.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                setDrawAxisLine(false)
            }
        }
    }
}