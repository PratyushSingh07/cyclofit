package com.example.cyclofit.ui.utils

import android.util.Log

object Tools {
    fun debugMessage(messsage:String, tag:String){
        Log.e(tag, messsage )
    }

    fun <T> convertListToArrayList(list: List<T>): ArrayList<T> {
        // convert List to ArrayList.
        // converting List to ArrayList id not available in kotlin
        val arrayList = ArrayList<T>()
        kotlin.run {
            for (l in list) {
                arrayList.add(l)
            }
        }
        return arrayList
    }
}