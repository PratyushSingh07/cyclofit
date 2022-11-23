package com.example.cyclofit.ui.firestore

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cyclofit.model.Post
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.fragment.*
import com.example.cyclofit.ui.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(fragment: SignUpFragment, userInfo: User) {

        //the "users" is a collection name.
        mFirestore.collection(Constants.USERS)
            //Document Id for users fields.
            .document(userInfo.id)
            //here the userInfo are field and the setOption is set to merge.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //Here call a function of base activity for transferring the result to it.

                fragment.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while registering the user_id.",
                    e
                )
            }
    }

    private fun getCurrentUserId(): String {
        //An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }

        return currentUserId
    }


    fun getUserDetails(fragment: Fragment){

        //Here we pass the collection name from which we wants the data.
        mFirestore.collection(Constants.USERS)
            // the document id to get the field of User.
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document->
                Log.i(fragment.javaClass.simpleName,document.toString())

                //Here we have received the document snapshot which is converted into the User data model object.
                val user = document.toObject(User::class.java)!!

//                val sharedPreferences = fragment.getSharedPreferences(
//                    Constants.CYCLOFIT_PREFERENCES,
//                    Context.MODE_PRIVATE
//                )
//
//                val editor : SharedPreferences.Editor = sharedPreferences.edit()
//                //key : logged_in_success :Aditya Gupta
//                editor.putString(Constants.LOGGED_IN_USERNAME," ${user.name}")
//                editor.apply()
//                //end

                when(fragment){
                    is SignInFragment ->
                    {
                        //call a function of base activity for transferring the result to it
                        fragment.userLoggedInSuccess(user)

                    }
                }
            }
    }

    fun registerUserRealTime(fragment: SignUpFragment,userInfo: User){

        FirebaseDatabase.getInstance().getReference(Constants.USERS)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(userInfo)
            .addOnSuccessListener {

            }
            .addOnFailureListener {e->
                fragment.hideProgressDialog()
                Log.e(
                    "Maa Baap ka pyaar check",
                    "Error while registering the user_id.",
                    e
                )
            }
    }

    fun getUserDetailsRealtime(fragment: CommunityFragment){

        val list = arrayListOf<User>()

        FirebaseDatabase.getInstance().getReference(Constants.USERS)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        for(data in snapshot.children){
                            val model = data.getValue(User::class.java)
                            if(model!!.id != FirebaseAuth.getInstance().uid){
                                list.add(model)
                            }
                        }
                        list.shuffle()
                    }
                    else{
                        Toast.makeText(fragment.requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                    }
                    fragment.userDetailsSuccess(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    fun createCommunity(fragment: CreateCommunityFragment, community : String){
        FirebaseDatabase.getInstance().getReference(Constants.COMMUNITY)
            .child(community)
            .setValue(community)
            .addOnSuccessListener {
                fragment.createCommunitySuccess()
            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(
                    "Maa Baap ka pyaar check",
                    "Error while registering the user_id.",
                    e
                )
            }
    }

    fun createPost(fragment: PostFragment,community: String,postList : ArrayList<Post>){
        FirebaseDatabase.getInstance().getReference(Constants.COMMUNITY)
            .child(community)
            .setValue(postList)
            .addOnSuccessListener {
                fragment.createPostSuccess()
            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(
                    "Maa Baap ka pyaar check",
                    "Error while registering the user_id.",
                    e
                )
            }
    }

}