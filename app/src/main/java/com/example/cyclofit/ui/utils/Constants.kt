package com.example.cyclofit.ui.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment

object Constants{

    const val USERS = "users"
    const val COMMUNITY = "community"
    const val LOGGED_IN_USERNAME = "name"
    const val CYCLOFIT_PREFERENCES : String = "cycloFit"
    const val ONBOARDING_FLAG : String = "no"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2

    const val EXTRA_USER_DETAILS = " "
    const val USER_PROFILE_IMAGE = " "
    const val NAME = "name"
    const val IMAGE = "image"
    const val MOBILE = "phone"
    const val WEIGHT = "weight"
    const val SOS = "sos"
    var bingo :String = ""
    const val PUT_EXTRA = "put"
    const val PROFILE_COMPLETED = "profile"


    fun showImageChooser(activity : Activity)
    {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?
    {

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }

    fun getFileExtensionFragment(fragment: Fragment, uri: Uri?):String?
    {

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(fragment.requireContext().contentResolver.getType(uri!!))

    }
}