package com.example.cyclofit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cyclofit.R
import com.example.cyclofit.ui.fragment.SignInFragment

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportActionBar?.hide()

        val fragmentSignIn = SignInFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView,fragmentSignIn)
        }
    }
}