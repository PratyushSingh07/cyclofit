package com.example.cyclofit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentCreateCommunityBinding
import com.example.cyclofit.ui.firestore.FirestoreClass

class CreateCommunityFragment : BaseFragment() {

    lateinit var binding : FragmentCreateCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreateCommunityBinding.inflate(inflater,container,false)


        val name = binding.etCommunityName.text.toString()

        binding.btnCreate.setOnClickListener {
            showProgressDialog()
            FirestoreClass().createCommunity(this,binding.etCommunityName.text.toString())
        }

        return binding.root
    }

    fun createCommunitySuccess(){
        hideProgressDialog()
        requireActivity().onBackPressed()
    }
}