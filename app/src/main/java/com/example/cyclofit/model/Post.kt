package com.example.cyclofit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Post(
    val name  : String = " ",
    val userImage : String = " ",
    val details : String = " ",
    val image : String = " ",
    val comm : String = " "
): Parcelable
