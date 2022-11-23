package com.example.cyclofit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Post(
//    val id  : String = " ",
    val details : String = " ",
//    val image : String = " ",
//    val comm : String = " "
): Parcelable
