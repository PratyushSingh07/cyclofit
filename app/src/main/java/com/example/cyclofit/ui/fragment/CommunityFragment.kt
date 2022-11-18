package com.example.cyclofit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentCommunityBinding
import com.example.cyclofit.ui.User
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

        getUserMessageList()

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