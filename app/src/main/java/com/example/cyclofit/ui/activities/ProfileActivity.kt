package com.example.cyclofit.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cyclofit.R
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.utils.Constants
import com.example.cyclofit.ui.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException

class ProfileActivity : BaseActivity() , View.OnClickListener {

    private lateinit var mUserDetails : User
    private var mSelectedImageFileUrl : Uri? =null
    private var mUserProfileImageURl :String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.hide()

        FirestoreClass().getUserDetails(this)

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS))
        {
            //get the user_id Details from intent as parcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

//        et_name_profile.setText(mUserDetails.name)

        val sharedPreferences = getSharedPreferences(Constants.CYCLOFIT_PREFERENCES, Context.MODE_PRIVATE)
        val check = sharedPreferences.getString(Constants.ONBOARDING_FLAG,"")!!

        if(check=="yes")
        {
            title_profile.text = "Complete Profile"
            et_name_profile.isEnabled = true
            iv_select_profile.setImageDrawable(resources.getDrawable(R.drawable.add_photo))

        }else{
            title_profile.text = "Edit Profile"
            GlideLoader(this).loadUserPicture(mUserDetails.image,user_image)
            iv_select_profile.setImageDrawable(resources.getDrawable(R.drawable.add_photo))


            if(mUserDetails.phone != 0L)
            {
                et_phoneNo_profile.setText(mUserDetails.phone.toString())
            }
            if(mUserDetails.weight.isNotEmpty())
            {
                et_weight_profile.setText(mUserDetails.weight)
            }
            if(mUserDetails.sos.isNotEmpty())
            {
                et_sos_profile.setText(mUserDetails.sos)
            }
            if(mUserDetails.name.isNotEmpty())
            {
                et_name_profile.setText(mUserDetails.name.toString())
            }
        }

        user_image.setOnClickListener(this)
        btn_done.setOnClickListener (this)
        iv_select_profile.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v!=null)
            when(v.id){

                R.id.user_image -> {

                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed we will request for the same.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
//                        showErrorSnackBar("You already have the storage permission.", false)
                    } else {

                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.iv_select_profile -> {

                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed we will request for the same.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
//                        showErrorSnackBar("You already have the storage permission.", false)
                    } else {

                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_done ->
                {
                    if(validateUserProfileDetails())
                    {

                        showProgressbar()

                        if(mSelectedImageFileUrl!=null)
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUrl)
                        else
                        {
                            updateUserProfileDetails()
                        }


                    }
                }
            }
    }

    private fun updateUserProfileDetails()
    {
        val userHashMap = HashMap<String,Any>()

        val name = et_name_profile.text.toString().trim{ it <= ' '}

        if(name != mUserDetails.name)
        {
            userHashMap[Constants.NAME]=name
        }

        val mobileNo = et_phoneNo_profile.text.toString().trim{ it <= ' '}
        val currentClass = et_weight_profile.text.trim{it <= ' '}
        val stream = et_sos_profile.text.toString().trim { it<=' '}


        if(mUserProfileImageURl.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURl
        }
        if(mobileNo.isNotEmpty() && mobileNo!=mUserDetails.phone.toString())
        {
            userHashMap[Constants.MOBILE] = mobileNo.toLong()
        }

        if(currentClass.isNotEmpty() && currentClass != mUserDetails.weight){
            userHashMap[Constants.WEIGHT] = currentClass.toString()
        }

        if(stream.isNotEmpty() && stream !=mUserDetails.sos){
            userHashMap[Constants.SOS] = stream
        }


//        showProgressbar(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this@ProfileActivity,userHashMap)

//      showErrorSnackBar("your Details are valid .",false)

    }

    fun userProfileUpdateSuccess(){
        hideProgressDialog()

        showErrorSnackBar("profile updated successfully",false)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
//                showErrorSnackBar("The storage permission is granted.", false)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,"Oops , you just denied the permission for storage .you can also allow in app permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        //the uri of selected image from phone storage.
                        mSelectedImageFileUrl = data.data!!
//                        user_image.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUrl!!, user_image)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Image selection Failed !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else if (resultCode== Activity.RESULT_CANCELED)
        {
            Log.e("request cancelled", "image selection  cancelled")
        }
    }

    private fun validateUserProfileDetails():Boolean {
        return when{
            TextUtils.isEmpty(et_phoneNo_profile.text.toString().trim{it <=' '})->
            {
                showErrorSnackBar("Enter your mobile number",true)
                false
            }
            TextUtils.isEmpty(et_weight_profile.text.toString().trim{it <=' '})->
            {
                showErrorSnackBar("Enter your Current Class",true)
                false
            }
            else->
            {
                true
            }
        }
    }

    fun imageUploadSuccess(imageUrl:String){

        hideProgressDialog()

        mUserProfileImageURl = imageUrl

        println("Yes image upload successfully")

        updateUserProfileDetails()
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }

    fun userLoggedInSuccess(user: User) {
        mUserDetails = user
    }
}