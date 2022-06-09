package com.slc.prototype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_class_home_screen.class_name_title
import kotlinx.android.synthetic.main.activity_class_settings.*

class ClassSettingsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_settings)
        auth = Firebase.auth
        var currentClassID: String? = null
        val bundle: Bundle? = intent.extras
        if (bundle != null){
            currentClassID = bundle.getString(CURRENTCLASSID)
        }

        val db = Firebase.firestore
        db.collection("Class").document(currentClassID!!)
            .get().addOnSuccessListener {
                val className = it.get("className")
                class_name_title.text = className.toString()
                val classDesc = it.get("classDesc")
                class_description.text = classDesc.toString()
                val classPicture = it.get("classPictureUrl")
                Glide.with(applicationContext).load(classPicture).into(class_picture)
                val classAdmin = it.get("classAdmin").toString()
            }
    }
}