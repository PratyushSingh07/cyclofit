package com.example.cyclofit.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentCommunityBinding
import com.example.cyclofit.ui.User
import com.example.cyclofit.ui.adapter.CommunityListAdapter
import com.example.cyclofit.ui.adapter.MessageUserAdapter
import com.example.cyclofit.ui.firestore.FirestoreClass
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_community.*


class CommunityFragment : BaseFragment() {

    lateinit var binding : FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater,container,false)

        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)

        getUserMessageList()
        val nameList= listOf<String>("ab","fe","efw","fhd","jr","3r","3r","ab","ab","ab","ab","ab","ab","ab","ab","ab","ab","ab")
        binding.rvCommunityName.adapter=CommunityListAdapter(nameList)
        binding.rvCommunityName.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
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