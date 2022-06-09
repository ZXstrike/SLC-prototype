package com.slc.prototype

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.user_profile_picture

const val CLASS_ID = "ClassID"

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val classList : ArrayList<ClassData> = ArrayList()
    private lateinit var classAdapter: ClassAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        val recyclerView : RecyclerView = findViewById(R.id.class_list)
        classAdapter = ClassAdapter(classList){
            onClickAdapter(it.getID().toString())
        }
        val layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = classAdapter
        prepareClassData()

    }

    fun onRadioButtonClicked(view: View){
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.class_button ->
                    if (checked) {
                        class_list.visibility = View.VISIBLE
                        task_list.visibility = View.GONE
                    }
                R.id.task_button ->
                    if (checked) {
                        class_list.visibility = View.GONE
                        task_list.visibility = View.VISIBLE
                    }
            }
        }
    }

    private fun onClickAdapter(classID :String){
        val intent = Intent(this, ClassHomeScreenActivity::class.java)
        intent.putExtra(CLASS_ID, classID)
        startActivity(intent)
    }

    private fun prepareClassData(){
        val db = Firebase.firestore
        val userId = auth.uid
        db.collection("Users").document(userId!!)
            .get()
            .addOnSuccessListener { document ->
                Glide.with(applicationContext).load(document.get("profilePictureUrl")).into(user_profile_picture)
                user_name.text = "Welcome, " +document.get("name").toString()
                val userClassList = document.get("userClassList") as List<*>
                userClassList.forEach { id ->
                    val classID = id.toString()
                    db.collection("Class").document(id.toString())
                        .get()
                        .addOnSuccessListener { document ->
                            val name = document.get("className").toString()
                            val image = document.get("classPictureUrl").toString()
                            val classData = ClassData(name, image, classID)
                            classList.add(classData)
                            classAdapter.notifyDataSetChanged()
                        }
                    }
                }.addOnCompleteListener{
                    progress_bar.visibility = View.GONE
            }
    }

    fun showMenu(view: View) {
        menu_list.visibility = View.VISIBLE
    }

    fun closeOverlay(view: View) {
        menu_list.visibility = View.GONE
    }

    /*fun addClass(view: View){
        add_class_ui.visibility = View.VISIBLE
    }*/

    fun goToSettings(view: View) {
        startActivity(Intent(this, ProfileActivity::class.java))
        menu_list.visibility = View.GONE
    }
}