package com.example.cyclofit.ui.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cyclofit.model.Post
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.activities.CreateCommunityActivity
import com.example.cyclofit.ui.activities.PostActivity
import com.example.cyclofit.ui.activities.ProfileActivity
import com.example.cyclofit.ui.fragment.*
import com.example.cyclofit.ui.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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


                val sharedPreferences = fragment.requireContext().getSharedPreferences(
                    Constants.CYCLOFIT_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME, user.name)
                editor.apply()

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

//    fun getUserDetailsRealtime(fragment: CommunityFragment){
//
//        val list = arrayListOf<User>()
//
//        FirebaseDatabase.getInstance().getReference(Constants.USERS)
//            .addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if(snapshot.exists()){
//
//                        for(data in snapshot.children){
//                            val model = data.getValue(User::class.java)
//                            if(model!!.id != FirebaseAuth.getInstance().uid){
//                                list.add(model)
//                            }
//                        }
//                        list.shuffle()
//                    }
//                    else{
//                        Toast.makeText(fragment.requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
//                    }
////                    fragment.getAllpostSuccess(list)
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//
//    }

    fun createCommunity(activity: CreateCommunityActivity, community : String){
        FirebaseDatabase.getInstance().getReference(Constants.COMMUNITY)
            .child(community)
            .setValue(community)
            .addOnSuccessListener {
                activity.createCommunitySuccess()
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(
                    "Maa Baap ka pyaar check",
                    "Error while registering the user_id.",
                    e
                )
            }
    }

    fun createPost(activity: PostActivity,community: String,postList : ArrayList<Post>){

        FirebaseDatabase.getInstance().getReference(Constants.COMMUNITY)
            .child(community)
            .setValue(postList)
            .addOnSuccessListener {
                activity.createPostSuccess()
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(
                    "Maa Baap ka pyaar check",
                    "Error while registering the user_id.",
                    e
                )
            }
    }

    fun getCommunityList(fragment: CommunityFragment){

        val list = ArrayList<String>()

        FirebaseDatabase.getInstance().getReference(Constants.COMMUNITY)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        for (data in snapshot.children) {
                            val key = data.key
                            list.add(key.toString())
                        }
                        list.shuffle()
                    }
                    else{
                        Toast.makeText(fragment.requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                    }
                    fragment.getCommunityListSuccess(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    fun getAllPost(fragment: CommunityFragment){

        val list = ArrayList<Post>()

        FirebaseDatabase.getInstance().getReference(Constants.COMMUNITY).child("abcd")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        for (data in snapshot.children) {
                            val value = data.getValue(Post::class.java)
                            list.add(value!!)
                            Log.e("fuck", value.toString())
                        }

                        list.shuffle()
                    }
                    else{
                        Toast.makeText(fragment.requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                    }
                    fragment.getAllPostSuccess(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "post" + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        when (activity) {
                            is PostActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is ProfileActivity ->{
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                        // END
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is PostActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    "ABCD",
                    exception.message,
                    exception
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnCompleteListener {

                println("Yes profile updated")
                when (activity) {
                    is ProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is ProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName, "Error while updating the user_id Details", e
                )
            }
    }

    fun getUserDetails(activity: Activity){

        //Here we pass the collection name from which we wants the data.
        mFirestore.collection(Constants.USERS)
            // the document id to get the field of User.
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName,document.toString())

                //Here we have received the document snapshot which is converted into the User data model object.
                val user = document.toObject(User::class.java)!!


                val sharedPreferences = activity.getSharedPreferences(
                    Constants.CYCLOFIT_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME, user.name)
                editor.apply()

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

                when(activity){
                    is ProfileActivity ->
                    {
                        //call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)

                    }
                }
            }
    }
}