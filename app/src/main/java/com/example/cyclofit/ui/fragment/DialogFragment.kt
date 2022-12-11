package com.example.cyclofit.ui.fragment

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import com.example.cyclofit.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.fragment_dialog.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*

class DialogFragment:androidx.fragment.app.DialogFragment() {
   var choosenTheme: String = "null"
    private var nightMode:Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView:View =  inflater.inflate(R.layout.fragment_dialog, container, false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        rootView.cancel_button.setOnClickListener{
            dismiss()
        }
        sharedPreferences = requireContext().getSharedPreferences("NightMode",0)
        nightMode = sharedPreferences.getBoolean("night",false)

        rootView.confirm_button.setOnClickListener {
            val selectedId = ThemeChooserRadioBtn.checkedRadioButtonId
            val radio = rootView.findViewById<RadioButton>(selectedId)
            val finalTheme = radio.id.toString()
            choosenTheme = finalTheme
            Log.d("adf",finalTheme)
            getSelectedTheme(finalTheme)
            dismiss()

        }
        return rootView
    }
    private fun getSelectedTheme(finalTheme: String){
        if(finalTheme == R.id.white.toString()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor = sharedPreferences.edit()
            editor.putBoolean("night",false)
        }
        else if(finalTheme == R.id.dark.toString()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor = sharedPreferences.edit()

            editor.putBoolean("night",true)
        }else{
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        }
        editor.apply()

    }


}