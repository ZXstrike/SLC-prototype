package com.slc.prototype

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val classList : ArrayList<ClassData> = ArrayList()
    private lateinit var classAdapter: ClassAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        val recyclerView : RecyclerView = findViewById(R.id.class_list)
        classAdapter = ClassAdapter(classList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = classAdapter
        prepareClassData()
    }

    private fun prepareClassData(){
        val db = Firebase.firestore
        val userId = auth.uid
        db.collection("Users").document(userId!!)
            .get()
            .addOnSuccessListener { document ->
                val userClassList = document.get("userClassList") as List<*>
                userClassList.forEach { id ->
                    db.collection("Class").document(id.toString())
                        .get()
                        .addOnSuccessListener {
                            val name = it.get("className").toString()
                            val image = it.get("classPictureUrl").toString()
                            db.collection("Users").document(it.get("classAdmin").toString())
                                .get()
                                .addOnSuccessListener {
                                    val teacher = it.get("name").toString()
                                    var classData = ClassData(name, teacher, image)
                                    classList.add(classData)
                                    classAdapter.notifyDataSetChanged()
                                }
                        }
                    }
                }


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