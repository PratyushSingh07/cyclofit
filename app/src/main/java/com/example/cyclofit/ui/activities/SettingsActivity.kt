package com.example.cyclofit.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.cyclofit.R
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.fragment.DialogFragment
import com.example.cyclofit.ui.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity :AppCompatActivity(){

    private lateinit var selectThemeButton:Button
    private var nightMode:Boolean = false
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var editor:Editor
    lateinit var dialogFragment: DialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        dialogFragment = DialogFragment()


        selectThemeButton = findViewById(R.id.changeThemeButton)
        changeAppTheme()

        val btn = findViewById<Button>(R.id.logoutButton)
        btn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this,AuthenticationActivity::class.java))
        }

        val exploreBtn=findViewById<AppCompatButton>(R.id.exploreBtn)
        exploreBtn.setOnClickListener {
            startActivity(Intent(this,AchievementsActivity::class.java))
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


    private fun changeAppTheme(){
        selectThemeButton.setOnClickListener {
            dialogFragment.show(supportFragmentManager,"customDialog")
//            editor = sharedPreferences.edit()
//            editor.putBoolean("isNewUser",false)
        }
    }


}