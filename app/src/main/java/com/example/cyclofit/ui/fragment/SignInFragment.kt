package com.example.cyclofit.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentSignInBinding
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.activities.DashboardActivity
import com.example.cyclofit.ui.activities.ProfileActivity
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.utils.Constants
import com.google.firebase.auth.FirebaseAuth


class SignInFragment : BaseFragment(){

    lateinit var binding : FragmentSignInBinding
    private lateinit var mUserDetails : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignInBinding.inflate(inflater,container,false)

        binding.tvSignUp.setOnClickListener {
            findNavController(it).navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController(it).navigate(R.id.action_signInFragment_to_forgetPasswordFragment)
        }
        binding.btnSignIn.setOnClickListener {
            logInRegisteredUser()
        }
        return binding.root
    }


    private fun logInRegisteredUser() {

        if(validateLoginDetails()){

            showProgressDialog()

            val email = binding.etEmailSignTn.text.toString().trim{ it <= ' '}
            val password = binding.etPasswordSignIn.text.toString().trim{ it <= ' '}

             FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            //Todo - send user_id to Main Activity
                            FirestoreClass().getUserDetails(this)
                            showErrorSnackBar("You are logged in successfully.", false)


//                    startActivity(Intent(this,MainActivity::class.java))
//                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }


    fun userLoggedInSuccess(user: User){

        hideProgressDialog()

        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.CYCLOFIT_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constants.ONBOARDING_FLAG,"yes")
        editor.apply()

        if(user.profile == 0) {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(requireActivity(), ProfileActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }, 200)
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(requireActivity(), DashboardActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }, 200)
        }
    }

    private fun validateLoginDetails():Boolean {

        return when {
            TextUtils.isEmpty(binding.etEmailSignTn.text.toString().trim{it<= ' '}) ->
            {
                showErrorSnackBar("Enter your email Id",true)
                false
            }
            TextUtils.isEmpty(binding.etPasswordSignIn.text.toString().trim{it<= ' '}) ->
            {
                showErrorSnackBar("Enter your password",true)
                false
            }
            else ->
            {
//                showErrorSnackBar("Your Details are valid.",false)
                true
            }
        }
    }
}