package com.example.cyclofit.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.cyclofit.ui.fragment.DistancePieChartFragment
import com.example.cyclofit.ui.fragment.KcalPeiChartFragment
import com.example.cyclofit.ui.fragment.TimePieCharFragment

class PieChartAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    LineChartAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> KcalPeiChartFragment()
            1 -> TimePieCharFragment()
            else -> DistancePieChartFragment()
        }
    }
}