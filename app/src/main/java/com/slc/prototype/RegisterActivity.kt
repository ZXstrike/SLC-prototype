package com.slc.prototype

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register"

        auth = Firebase.auth
    }

    fun register(view: View){
        val name = reg_username_field.text.toString()
        val email = reg_email_field.text.toString()
        val password = reg_password_field.text.toString()
        when {
            name == "" ->{
                Toast.makeText(this, "Please fill all form!", Toast.LENGTH_SHORT).show()
            }
            email == "" -> {
                Toast.makeText(this, "Please fill all form!", Toast.LENGTH_SHORT).show()
            }
            password == "" -> {
                Toast.makeText(this, "Please fill all form!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                progressBarScreen.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val db = Firebase.firestore
                        val userData = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "userId" to auth.uid,
                            "birthDate" to "DD/MM/YYYY",
                            "phoneNumber" to "",
                            "schoolName" to "",
                            "userClassList" to arrayListOf<String>(),
                            "profilePictureUrl" to "https://firebasestorage.googleapis.com/v0/b/slc-prototype.appspot.com/o/SLC%20Logo-07.png?alt=media&token=2030e52b-0cc4-4fb0-ac6a-ce9dd5f0c52b",
                        )
                        db.collection("Users").document(auth.uid!!)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot added with ID: ${auth.uid}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }

                        updateUI()
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    progressBarScreen.visibility = View.GONE
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    fun goToLogin(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUI(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}