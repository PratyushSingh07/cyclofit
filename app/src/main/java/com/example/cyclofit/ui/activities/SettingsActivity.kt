package com.example.cyclofit.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cyclofit.R
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.fragment.DialogFragment
import com.example.cyclofit.ui.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity :AppCompatActivity(){

    private lateinit var selectThemeBtn:Button
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


        selectThemeBtn = findViewById(R.id.changeThemeButton)
        changeAppTheme()
//        //getTheDarkMode
//        sharedPreferences = getSharedPreferences("NightMode",0)
//        nightMode = sharedPreferences.getBoolean("isNewUser",false)
//        getDarkMode(nightMode)

        val btn = findViewById<Button>(R.id.logoutButton)
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


    private fun changeAppTheme(){
        selectThemeBtn.setOnClickListener {
            dialogFragment.show(supportFragmentManager,"customDialog")
//            editor = sharedPreferences.edit()
//            editor.putBoolean("isNewUser",false)
        }
    }


}