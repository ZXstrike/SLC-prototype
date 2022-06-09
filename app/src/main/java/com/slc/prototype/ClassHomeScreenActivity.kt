package com.slc.prototype

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_class_home_screen.*
import kotlinx.android.synthetic.main.activity_class_home_screen.class_name_title
import kotlinx.android.synthetic.main.activity_class_settings.*
import kotlinx.android.synthetic.main.activity_home.*

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

    fun onRadioButtonClicked(view: View){
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.class_button ->
                    if (checked) {
                        material_list.visibility = View.VISIBLE
                        task_list.visibility = View.GONE
                    }
                R.id.task_button ->
                    if (checked) {
                        material_list.visibility = View.GONE
                        class_task_list.visibility = View.VISIBLE
                    }
            }
        }
    }

    fun backToHome(view: View) {
        onBackPressed()
    }

    fun postSelection(view: View) {
        post_selection.visibility = View.VISIBLE
    }

    fun closeOverlay(view: View) {
        post_selection.visibility = View.GONE
    }

    fun goToPostMaterial(view: View) {
        val intent = Intent(this, MaterialPostActivity::class.java)
        startActivity(intent)
    }

    fun goToPostTask(view: View) {
        val intent = Intent(this, TaskPostActivity::class.java)
        startActivity(intent)
    }
}