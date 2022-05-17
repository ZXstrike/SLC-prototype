package com.slc.prototype

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "login"

        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null){
            reload()
        }
    }

    fun login(view: View){
        val email = log_email_field.text.toString()
        val password = log_password_field.text.toString()
        when {
            email == "" -> {
                Toast.makeText(this, "You did not enter an email", Toast.LENGTH_SHORT).show()
            }
            password == "" -> {
                Toast.makeText(this, "You did not enter a password", Toast.LENGTH_SHORT).show()
            }
            else -> {
                progressBarScreen.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = Firebase.auth.currentUser
                        updateUI(user)
                        finish()
                    }else{
                        progressBarScreen.visibility = View.GONE
                        Toast.makeText(baseContext, "This user not registered", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun goToRegister(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun reload() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}