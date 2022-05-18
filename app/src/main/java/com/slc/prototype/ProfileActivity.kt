package com.slc.prototype

import android.content.ContentValues.TAG
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        title = "profile"

        auth = Firebase.auth
        val db = Firebase.firestore
        val userData = auth.uid

        val docRef = db.collection("Users").document(userData!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val name = document.get("name").toString()
                    user_full_name.text = name
                    val url = document.get("profilePictureUrl").toString()
                    Glide.with(applicationContext).load(url).into(user_profile_picture)
                    val phoneNumber = document.get("phoneNumber").toString()
                    phone_number.text = phoneNumber
                }else{
                    Log.d(TAG, "No such document")
                }
            }.addOnFailureListener{ exception ->
                Log.d(TAG, "get failed with", exception)
            }
    }

    override fun onResume() {
        super.onResume()

        val db = Firebase.firestore
        val userData = auth.uid
        val docRef = db.collection("Users").document(userData!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val name = document.get("name").toString()
                    user_full_name.text = name
                    val url = document.get("profilePictureUrl").toString()
                    Glide.with(applicationContext).load(url).into(user_profile_picture)
                    val schoolName = document.get("schoolName").toString()
                    user_school_name.text = schoolName
                    val phoneNumber = document.get("phoneNumber").toString()
                    phone_number.text = phoneNumber
                }else{
                    Log.d(TAG, "No such document")
                }
            }.addOnFailureListener{ exception ->
                Log.d(TAG, "get failed with", exception)
            }
    }

    fun editProfile(view: View){
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
        onPause()
    }

    fun createClass(view: View){
        val intent = Intent(this, CreateClassActivity::class.java)
        startActivity(intent)
        add_class_ui.visibility = View.GONE
    }

    fun joinClass(view: View){
        val intent = Intent(this, CreateClassActivity::class.java)
        startActivity(intent)
        add_class_ui.visibility = View.GONE
    }

    fun addClass(view: View){
        add_class_ui.visibility = View.VISIBLE
    }

    fun closeOverlay(view: View){
        add_class_ui.visibility = View.GONE
    }

    fun home(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun folder(view: View){
        val intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun chat(view: View){
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun profile(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun logout(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Firebase.auth.signOut()
        finish()
    }
}