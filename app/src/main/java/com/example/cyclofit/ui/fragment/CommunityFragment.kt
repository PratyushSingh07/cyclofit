package com.example.cyclofit.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentCommunityBinding
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.adapter.CommunityListAdapter
import com.example.cyclofit.ui.adapter.MessageUserAdapter
import com.example.cyclofit.ui.firestore.FirestoreClass


class CommunityFragment : BaseFragment() {

    lateinit var binding : FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater,container,false)

        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        binding.fabNewCommunity.setOnClickListener {
            val createFragment = CreateCommunityFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_dashboard,createFragment)
                .commit()
        }

        getUserMessageList()
        val nameList= listOf<String>("ab","fe","efw","fhd","jr","3r","3r","ab","ab","ab","ab","ab","ab","ab","ab","ab","ab","ab")
        binding.rvCommunityName.adapter=CommunityListAdapter(nameList)
        binding.rvCommunityName.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.fab.setOnClickListener{
            val postFragment=PostFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_dashboard,postFragment)
                .commit()
        }
        return binding.root
    }

    private fun getUserMessageList() {
        showProgressDialog()
        FirestoreClass().getUserDetailsRealtime(this)
    }

    fun userDetailsSuccess(list: ArrayList<User>) {

        hideProgressDialog()

        binding.rvCommunityMember.adapter = MessageUserAdapter(requireContext(),list)
    }
}