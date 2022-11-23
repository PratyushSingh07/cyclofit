package com.example.cyclofit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Community(
    val key : String = " ",
    val value :ArrayList<Post>
):Parcelable
