package com.example.cyclofit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentHomeBinding
import com.example.cyclofit.databinding.FragmentPostBinding

class PostFragment : Fragment() {

    lateinit var binding: FragmentPostBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPostBinding.inflate(inflater,container,false)
        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        return binding.root
    }
}