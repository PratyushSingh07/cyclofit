package com.example.cyclofit.ui.activities

import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.cyclofit.R
import com.example.cyclofit.model.Post
import com.example.cyclofit.ui.firestore.FirestoreClass
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : BaseActivity() {

    private val RESULT_LOAD_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        postImage.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }


        btn_post.setOnClickListener {
            showProgressbar()

            val postList = ArrayList<Post>()

            val posts = Post(
                details = postDescription.text.toString()
            )
            postList.add(posts)
            FirestoreClass().createPost(this,"abcd",postList)
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
        }
    }

    fun createPostSuccess(){
        hideProgressDialog()
        onBackPressed()
    }
}