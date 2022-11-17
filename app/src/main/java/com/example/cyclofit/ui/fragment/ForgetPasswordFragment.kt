package com.example.cyclofit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment : BaseFragment() {

    lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentForgetPasswordBinding.inflate(inflater,container,false)

        binding.btnReset.setOnClickListener {
            val email :String =binding.etEmailForgetPassword.text.toString().trim{ it<= ' '}

            if (email.isEmpty())
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_email),true)
            }
            else
            {
                showProgressDialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task->
                    hideProgressDialog()

                    if(task.isSuccessful)
                    {
                        Toast.makeText(requireActivity(),"Email sent successfully to reset your password !",
                            Toast.LENGTH_LONG).show()
                        requireActivity().onBackPressed()
                    }
                    else
                    {
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
            }
        }

        return binding.root
    }
}