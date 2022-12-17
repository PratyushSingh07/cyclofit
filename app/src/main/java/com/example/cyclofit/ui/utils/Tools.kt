package com.example.cyclofit.ui.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import com.example.cyclofit.R

object Tools {
    fun debugMessage(message: String, tag: String = "DEBUG-MESSAGE") {
        Log.e(tag, message)
    }

    fun popUpWindow(
        context: Context,
        fragmentView: View,
        layout: Int,
        gravity: Int = Gravity.BOTTOM,
        function: ((View, PopupWindow) -> Unit)? = null,
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(layout, null)
        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        popupWindow.elevation = 10.0F
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val slideIn = Slide()
        val slideOut = Slide()
        slideIn.slideEdge = Gravity.TOP
        slideOut.slideEdge = Gravity.END
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            popupWindow.enterTransition = slideIn
            popupWindow.exitTransition = slideOut
        }
        if (function != null) {
            function(view, popupWindow)
        }
        popupWindow.showAtLocation(fragmentView, gravity, 0, 0)
    }

    fun <T> convertListToArrayList(list: List<T>): ArrayList<T> {
        val arrayList = ArrayList<T>()
        kotlin.run {
            for (l in list) {
                arrayList.add(l)
            }
        }
        return arrayList
    }

}