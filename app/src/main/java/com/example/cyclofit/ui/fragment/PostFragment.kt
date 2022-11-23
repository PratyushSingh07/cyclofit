package com.example.cyclofit.ui.fragment

import android.R
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cyclofit.databinding.FragmentPostBinding
import com.example.cyclofit.model.Post
import com.example.cyclofit.ui.firestore.FirestoreClass
import com.google.firebase.database.FirebaseDatabase


class PostFragment : BaseFragment() {

    lateinit var binding: FragmentPostBinding
    private val RESULT_LOAD_IMAGE = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPostBinding.inflate(inflater,container,false)
        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.darker_gray)
        binding.postImage.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

        binding.btnPost.setOnClickListener {
            showProgressDialog()

            val postList = ArrayList<Post>()

            val posts = Post(
                details = binding.postDescription.text.toString()
            )
            postList.add(posts)
            FirestoreClass().createPost(this,"abcd",postList)
        }

        return binding.root
    }
      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                activity?.contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            val picturePath = cursor?.getString(columnIndex!!)
            cursor?.close()
            binding.postImage.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }

    fun createPostSuccess(){
        hideProgressDialog()
        requireActivity().onBackPressed()
    }

}
