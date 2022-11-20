package com.example.cyclofit.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.R
import androidx.navigation.Navigation.findNavController
import com.example.cyclofit.databinding.FragmentHomeBinding
import com.example.cyclofit.ui.activities.SettingsActivity


class HomeFragment : BaseFragment() {

    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        binding.userImage.setOnClickListener {
            startActivity(Intent(context,SettingsActivity::class.java))
        }

        return binding.root
    }
}