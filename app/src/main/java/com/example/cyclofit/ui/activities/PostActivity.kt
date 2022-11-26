package com.example.cyclofit.ui.activities

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.example.cyclofit.R
import com.example.cyclofit.model.Post
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.example.cyclofit.ui.utils.Constants
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : BaseActivity() {

    private val RESULT_LOAD_IMAGE = 1
    lateinit var mPostImage : Uri
    lateinit var postList : ArrayList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        supportActionBar?.hide()

        FirestoreClass().getAllPost(this)

        postImage.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }


        fab_post.setOnClickListener {
            showProgressbar()

            FirestoreClass().uploadImageToCloudStorage(this,mPostImage)
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
            mPostImage = data.data!!
        }
    }

    fun createPostSuccess(){
        hideProgressDialog()
        onBackPressed()
    }


    fun imageUploadSuccess(uri: String) {
//        val postList = ArrayList<Post>()

        val sharedPreferences = getSharedPreferences(Constants.CYCLOFIT_PREFERENCES, Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")!!

        val posts = Post(
            name = userName,
            details = postDescription.text.toString(),
            image = uri,
        )
        postList.add(posts)
        FirestoreClass().createPost(this,"abcd",postList)
    }

    fun getAllPostSuccess(list: ArrayList<Post>) {
        postList = list
    }
}