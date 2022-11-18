package com.example.cyclofit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}