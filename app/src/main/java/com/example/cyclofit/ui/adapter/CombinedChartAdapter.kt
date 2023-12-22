package com.example.cyclofit.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.cyclofit.ui.fragment.DistanceCombineChartFragment
import com.example.cyclofit.ui.fragment.KcalCombinedChartFragment
import com.example.cyclofit.ui.fragment.TimeCombinedChartFragment

class CombinedChartAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    LineChartAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> KcalCombinedChartFragment()
            1 -> TimeCombinedChartFragment()
            else -> DistanceCombineChartFragment()
        }
    }
}