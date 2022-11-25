package com.example.cyclofit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    val id : String = " ",
    val name : String = " ",
    val email : String = " ",
    val distance: String=" ",
    val phone :Long = 0L,
    val sos:String =" ",
    val weight : String = " ",
    val image : String = " "
):Parcelable
