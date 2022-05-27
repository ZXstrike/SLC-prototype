package com.slc.prototype

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_class_home_screen.*
import kotlinx.android.synthetic.main.class_card_layout.*

const val CURRENTCLASSID = "currentID"
class ClassHomeScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var currentClassID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_home_screen)
        auth = Firebase.auth
        val bundle: Bundle? = intent.extras
        if (bundle != null){
            currentClassID = bundle.getString(CLASS_ID)
        }

        val db = Firebase.firestore
        db.collection("Class").document(currentClassID!!)
            .get().addOnSuccessListener {
                val className = it.get("className")
                class_name_title.text = className.toString()
                val classPicture = it.get("classPictureUrl")
                Glide.with(applicationContext).load(classPicture).into(class_picture)
                val classAdmin = it.get("classAdmin").toString()
                if (auth.uid == classAdmin){
                    post_button.visibility = View.VISIBLE
                }
            }
     }

    fun classSettings(view: View) {
        val intent = Intent(this, ClassSettingsActivity::class.java)
        intent.putExtra(CURRENTCLASSID, currentClassID)
        startActivity(intent)
    }
}