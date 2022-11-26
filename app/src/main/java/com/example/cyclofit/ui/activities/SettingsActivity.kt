package com.example.cyclofit.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.cyclofit.R
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.utils.GlideLoader
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val btn =findViewById<MaterialButton>(R.id.logoutButton)
        btn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this,AuthenticationActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        GlideLoader(this).loadUserPicture(user.image,profileIcon)
        tv_name.text = user.name
        tv_email.text = user.email

    }
}