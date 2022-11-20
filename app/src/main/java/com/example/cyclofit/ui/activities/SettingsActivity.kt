package com.example.cyclofit.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cyclofit.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        val btn =findViewById<MaterialButton>(R.id.logoutButton)
        btn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this,AuthenticationActivity::class.java))
        }
    }
}