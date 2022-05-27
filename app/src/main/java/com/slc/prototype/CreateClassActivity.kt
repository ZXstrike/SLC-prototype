package com.slc.prototype

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_class.*
import java.util.*

class CreateClassActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_class)

        auth = Firebase.auth
    }

    fun addClassPicture(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    fun changeClassBanner(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private var classPhotoUri: Uri? = null
    private var classBannerUri: Uri? = null
    private var classDatabaseId: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            classPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, classPhotoUri)
            Glide.with(applicationContext).load(bitmap).into(class_profile)
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){

            classBannerUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, classBannerUri)
            Glide.with(applicationContext).load(bitmap).into(class_banner)
        }
    }

    private fun uploadImageToFirebaseStorage(){
        if (classPhotoUri == null) return
        if (classBannerUri == null) return
        if (classDatabaseId == null) return

        val pictureName = UUID.randomUUID().toString()
        val bannerName = UUID.randomUUID().toString()
        val refPicture = FirebaseStorage.getInstance().getReference("/Images/Class/$pictureName")
        val refBanner = FirebaseStorage.getInstance().getReference("/Images/Class/$bannerName")
        val dataBaseRef = Firebase.firestore.collection("Class").document(classDatabaseId!!)

        refPicture.putFile(classPhotoUri!!)
            .addOnSuccessListener {
                dataBaseRef.get().addOnSuccessListener { document ->
                    if (document != null){
                        refPicture.downloadUrl.addOnSuccessListener {
                            dataBaseRef.update("classPictureUrl", it.toString())
                        }
                    }
                }
            }
        refBanner.putFile(classBannerUri!!)
            .addOnSuccessListener {
                dataBaseRef.get().addOnSuccessListener { document ->
                    if (document != null){
                        refBanner.downloadUrl.addOnSuccessListener {
                            dataBaseRef.update("classBannerUrl", it.toString())
                        }
                    }
                }
            }
    }

    fun backToProfile(view: View) {
        onBackPressed()
    }

    fun confirmChange(view: View) {
        val userid = auth.uid
        val classId = UUID.randomUUID().toString()
        val className = class_name.text.toString()
        val classDesc = class_description.text.toString()
        val newClassData = hashMapOf(
            "className" to className,
            "classDesc" to classDesc,
            "classAdmin" to userid,
            "classMember" to arrayListOf<String>(),
            "fileList"  to arrayListOf<String>()
        )
        val userRef = Firebase.firestore.collection("Users").document(userid!!)
        val classRef = Firebase.firestore.collection("Class").document(classId)
        userRef.update("userClassList", FieldValue.arrayUnion(classId))
            .addOnSuccessListener {
                classRef
                    .set(newClassData).addOnSuccessListener {
                        classDatabaseId = classId
                        uploadImageToFirebaseStorage()
                    }
            }
        Handler().postDelayed({
            onBackPressed()
        },0)
    }
}