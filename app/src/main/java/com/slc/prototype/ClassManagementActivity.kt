package com.slc.prototype

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_class_management.*

class ClassManagementActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_management)
    }

    fun showOrHide(view: View) {
        val menuView = class_menu.visibility
        if (menuView != View.VISIBLE){
                class_menu.visibility = View.VISIBLE
                add_button.rotation = 45f
            }else{
                class_menu.visibility = View.GONE
                add_button.rotation = 0f
        }
    }

    fun addClass(view: View){
        val intent = Intent(this, AddClassActivity::class.java)
        startActivity(intent)
    }

    fun createClass(view: View){
        val intent = Intent(this, CreateClassActivity::class.java)
        startActivity(intent)
    }

    fun backToProfile(view: View){
        onBackPressed()
    }
}