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
import com.example.cyclofit.ui.firestore.FirestoreClass

class LeaderboardFragment : BaseFragment() {

    private lateinit var list : ArrayList<User>
    private lateinit var binding: FragmentLeaderboardBinding

    companion object{
        var list=ArrayList<User>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentLeaderboardBinding.inflate(inflater,container,false)
        binding.toolbarDashboard.inflateMenu(R.menu.leaderboard_top)
        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)


         list=ArrayList<User>()
        list.add(User("1","Pratyush","aries.@gmail.com","8.4"))
        list.add(User("2","Ayush","aries.@gmail.com","7.2"))
        list.add(User("3","Abhiram","aries.@gmail.com","6.8"))
        list.add(User("4","Aditya","aries.@gmail.com","5.3"))
        list.add(User("5","Samyak","aries.@gmail.com","5.1"))
        list.add(User("6","Rahul","aries.@gmail.com","4.9"))
        list.add(User("7","Ankur","aries.@gmail.com","3.0"))
        list.add(User("8","Adiii","aries.@gmail.com","2.5"))
        list.add(User("9","Prince","aries.@gmail.com","1.7"))
        list.add(User("10","Ritik","aries.@gmail.com","0.8"))



        return binding.root
    }

    fun getLeaderBoard(userList: ArrayList<User>) {

        hideProgressDialog()

        binding.rvLeaderboard.adapter=LeaderboardAdapter(requireContext(),userList)
        binding.rvLeaderboard.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FirestoreClass().getLeaderboardFragment(this)
    }
}