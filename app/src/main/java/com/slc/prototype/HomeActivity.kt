package com.slc.prototype

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
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
}