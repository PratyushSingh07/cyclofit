package com.example.cyclofit.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cyclofit.ui.fragment.DistanceFragment
import com.example.cyclofit.ui.fragment.KcalFragment
import com.example.cyclofit.ui.fragment.TimeFragment

class ViewAdapter(fragmentManager: FragmentManager,lifecycle : Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return KcalFragment()
            1 -> return TimeFragment()
            else -> return DistanceFragment()
        }
    }
}