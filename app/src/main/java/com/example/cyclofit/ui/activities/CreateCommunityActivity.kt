package com.example.cyclofit.ui.activities

import android.os.Bundle
import com.example.cyclofit.R
import com.example.cyclofit.ui.firestore.FirestoreClass
import kotlinx.android.synthetic.main.activity_create_community.*

class CreateCommunityActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)


        val name = et_community_name.text.toString()

        btn_create.setOnClickListener {
            showProgressbar()
            FirestoreClass().createCommunity(this,et_community_name.text.toString())
        }
    }

    fun createCommunitySuccess(){
        hideProgressDialog()
        onBackPressed()
    }
}