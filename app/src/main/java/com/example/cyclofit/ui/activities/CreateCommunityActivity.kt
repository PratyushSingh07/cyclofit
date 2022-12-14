package com.example.cyclofit.ui.activities

import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.example.cyclofit.R
import com.example.cyclofit.ui.firestore.FirestoreClass
import kotlinx.android.synthetic.main.activity_create_community.*
import kotlinx.android.synthetic.main.activity_post.*

class CreateCommunityActivity : BaseActivity() {

    private val RESULT_LOAD_IMAGE = 1
    lateinit var mBannerImage : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)

        supportActionBar?.hide()


        iv_select_community.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

        fab_create.setOnClickListener {
            showProgressbar()
            FirestoreClass().createCommunity(this,et_community_name.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            val picturePath = cursor?.getString(columnIndex!!)
            cursor?.close()
            postImage.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            mBannerImage = data.data!!
        }
    }

    fun createCommunitySuccess(){
        hideProgressDialog()
        onBackPressed()
    }
}