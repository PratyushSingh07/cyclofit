package com.example.cyclofit.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentSignUpBinding
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment() {

    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpBinding.inflate(inflater,container,false)

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }

        return binding.root
    }

    private fun registerUser()
    {
        if(validateRegisterDetails())
        {
            showProgressDialog()

            val email :String = binding.etEmailSignup.text.toString().trim{ it <= ' ' }
            val password : String = binding.etPasswordSignup.text.toString().trim{ it <= ' '}


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>{ task ->

                    hideProgressDialog()

                    if(task.isSuccessful){

                        val firebaseUser : FirebaseUser = task.result!!.user!!

                        val user = User(
                            id = firebaseUser.uid,
                            name = binding.etNameSignup.text.toString().trim{ it<=' ' },
                            email = binding.etEmailSignup.text.toString().trim{ it <= ' '},
                        )
                        FirestoreClass().registerUserRealTime(this,user)
                        FirestoreClass().registerUser(this, user)


//                        FirebaseAuth.getInstance().signOut()
//                        finish()
                    }
                    else
                    {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
            )
        }
    }

    private fun validateRegisterDetails():Boolean {
        return when {
            TextUtils.isEmpty(binding.etNameSignup.text.toString().trim{it<= ' '}) ->
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_first_name),true)
                false
            }
            TextUtils.isEmpty(binding.etEmailSignup.text.toString().trim{it<= ' '}) ->
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_email),true)
                false
            }
            TextUtils.isEmpty(binding.etPasswordSignup.text.toString().trim{it<= ' '}) ->
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_password),true)
                false
            }
            !cb_terms_and_condition.isChecked ->
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_checkbox_confirm),true)
                false
            }
            else ->
            {
                true
            }
        }
    }

    fun userRegistrationSuccess(){

        hideProgressDialog()
        showErrorSnackBar("You are registered successfully.",false)

        Handler(Looper.getMainLooper()).postDelayed({
            FirebaseAuth.getInstance().signOut()
            requireActivity().onBackPressed()
        },1000)
    }
}