package com.example.cyclofit.ui.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants{

    const val USERS = "users"
    const val COMMUNITY = "community"
    const val LOGGED_IN_USERNAME = "name"
    const val CYCLOFIT_PREFERENCES : String = "cycloFit"
    const val ONBOARDING_FLAG : String = "no"
    private const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2


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
}