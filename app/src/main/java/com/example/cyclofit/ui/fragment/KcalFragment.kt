package com.example.cyclofit.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentKcalBinding
import com.example.cyclofit.model.Shared
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.utils.Constants
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_kcal.*
import java.lang.reflect.Type


class KcalFragment : Fragment() {
    lateinit var binding: FragmentKcalBinding
    private var userWeight = ""
    private lateinit var mFireStore : FirebaseFirestore
    private var sp = ArrayList<Shared>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFireStore = FirebaseFirestore.getInstance()
        binding= FragmentKcalBinding.inflate(inflater,container,false)
        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.CYCLOFIT_PREFERENCES,
            Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("timer",null)
        val type: Type = object : TypeToken<ArrayList<Shared?>?>() {}.type

        //This is for accessing the user data form Firestore collection
        mFireStore.collection(Constants.USERS)
            // the document id to get the field of User.
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {
               userWeight = it.toObject(User::class.java)!!.weight.toDouble().toString()
                if(json!=null) {
                    sp = gson.fromJson(json, type)
                }
                //here the user weight is passed in the getCalories and after calculation the chart is shown
                val lineDataSet= LineDataSet(getCalories(userWeight),"My Graph")
                val lineData= LineData(lineDataSet)
                binding.kcalChart.data=lineData
                lineDataSet.color = getColor(resources,R.color.purple_200,resources.newTheme())
                lineDataSet.valueTextColor= Color.WHITE
                lineDataSet.valueTextSize=16f
                val axisLeft = binding.kcalChart.axisLeft.textColor
                binding.kcalChart.axisLeft
                lineDataSet.addColor(R.color.white)
                binding.kcalChart.description.isEnabled=true
                binding.kcalChart.invalidate()
            }


        return binding.root
    }

    private fun getCalories(userWeight: String): MutableList<Entry> {
        val barArrayList=mutableListOf<Entry>()
        for((x, i) in sp.withIndex()){
            val y=i.time.toInt()
            val met=12 // for bicycles
            val wt=userWeight.toDouble() // in kg
            var totalCalsBurnt=0.0;
            totalCalsBurnt=y*(met*3.5*wt)
            totalCalsBurnt/=200*1000;
            barArrayList.add(BarEntry(x*1f,totalCalsBurnt.toFloat()*1f))
        }
        return barArrayList
    }
}