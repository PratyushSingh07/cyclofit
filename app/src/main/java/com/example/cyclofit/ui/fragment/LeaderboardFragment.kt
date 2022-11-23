package com.example.cyclofit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentLeaderboardBinding
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.adapter.LeaderboardAdapter

class LeaderboardFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentLeaderboardBinding.inflate(inflater,container,false)
        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)
        var list=ArrayList<User>()
        list.add(User("1","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("2","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("3","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("4","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("5","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("6","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("7","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("8","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("9","Pratyush","aries.@gmail.com","8.4km"))
        list.add(User("10","Pratyush","aries.@gmail.com","8.4km"))
        binding.rvLeaderboard.adapter=LeaderboardAdapter(requireContext(),list)
        binding.rvLeaderboard.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        return binding.root
    }
}