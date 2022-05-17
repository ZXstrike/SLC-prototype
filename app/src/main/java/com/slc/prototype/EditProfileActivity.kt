package com.slc.prototype

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val db = Firebase.firestore
        val userData = auth.uid
        val docRef = db.collection("Users").document(userData!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    val name = document.get("name").toString()
                    edit_username.hint = name
                    val phoneNumber = document.get("phoneNumber").toString()
                    edit_phoneNumber.hint = phoneNumber
                    val schoolName = document.get("schoolName").toString()
                    edit_schoolName.hint = schoolName
                    val url = document.get("profilePictureUrl").toString()
                    Glide.with(applicationContext).load(url).into(user_profile_picture)
                }else{
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
        setContentView(R.layout.activity_edit_profile)
    }
    fun changeProfilePicture(view: View) {
        Log.d("EditProfileActivity", "Try to show photo selector")

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            Glide.with(applicationContext).load(bitmap).into(user_profile_picture)
        }
    }

    private fun uploadImageToFirebaseStorage(){
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/Images/Users/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                val userData = auth.uid
                val dataBaseRef = Firebase.firestore.collection("Users").document(userData!!)
                dataBaseRef.get().addOnSuccessListener { document ->
                    if (document != null){
                        val oldImageUrl = document.get("profilePictureUrl").toString()
                        val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl)
                        imageRef.delete()

                        ref.downloadUrl.addOnSuccessListener {
                            dataBaseRef.update("profilePictureUrl", it.toString())
                        }
                    }
                }
            }
    }

    fun confirmChange(view: View) {
        val db = Firebase.firestore
        val userData = auth.uid

        val newName = edit_username.text.toString()
        val newPhoneNumber = edit_phoneNumber.text.toString()
        val newSchoolName = edit_schoolName.text.toString()
         if(newName != ""){
            db.collection("Users").document(userData!!).update("name", newName)
        }
        if(newPhoneNumber != "") {
            db.collection("Users").document(userData!!).update("phoneNumber", newPhoneNumber)
        }
        if(newSchoolName != "") {
            db.collection("Users").document(userData!!).update("schoolName", newSchoolName)
        }
        uploadImageToFirebaseStorage()
        onBackPressed()
        onStart()
    }

    fun backToProfile(view: View) {
        onBackPressed()
    }
}