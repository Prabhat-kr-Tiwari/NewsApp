package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityMyProfileBinding
import com.example.newsapp.model.UserModel
import com.example.newsapp.utils.Config
import com.example.newsapp.utils.Config.hideDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MyProfileActivity : AppCompatActivity() {

    lateinit var binding:ActivityMyProfileBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var currentUserdatabaseReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var imageUri:Uri?=null
    private val selectedImage=registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri=it
        binding.imageView.setImageURI(imageUri)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        //getting the user data
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString())
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val userdata=snapshot.getValue(UserModel::class.java)
                    binding.edtUsername.setText(userdata?.username)
                    binding.edtFirstname.setText(userdata?.firstName)
                    binding.edtLastname.setText(userdata?.lastName)
                    binding.edtEmailid.setText(userdata?.emailId)
                    Glide.with(this@MyProfileActivity).load(userdata?.image).into(binding.imageView)


                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        binding.backBtn.setOnClickListener {
            finish()
        }
        //setting the image
        binding.imageView.setOnClickListener {
            selectedImage.launch("image/*")

        }
        binding.btnUpdate.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (binding.edtUsername.text.toString().isEmpty()
            ||binding.edtLastname.text.toString().isEmpty()
            ||binding.edtFirstname.text.toString().isEmpty()
            ||binding.edtEmailid.text.toString().isEmpty()
            ||imageUri==null){
            Toast.makeText(this, "Please Enter all fields", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this,"Updating....")

        val storageRef=FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {

                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener {
                    hideDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                hideDialog()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }


    }

    private fun storeData(imageUrl: Uri?) {

        databaseReference=FirebaseDatabase.getInstance().getReference("Users")
        currentUserdatabaseReference=FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser?.uid.toString())

        var phoneNumber: String? = null
        currentUserdatabaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                     phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java)
                    Log.d(TAG, "onDataChange: $phoneNumber")


                }
                val data=UserModel(

                    auth.uid.toString(),
                    image = imageUrl?.toString(),
                    username = binding.edtUsername.text.toString(),
                    phoneNumber = phoneNumber,
                    firstName = binding.edtFirstname.text.toString(),
                    lastName = binding.edtLastname.text.toString(),
                    emailId = binding.edtEmailid.text.toString(),
                    userType = "",
                    proofDocument = ""
                )
                databaseReference.child(auth.currentUser?.uid.toString()).setValue(data).addOnCompleteListener {

                    hideDialog()
                    if (it.isSuccessful){

                        Toast.makeText(this@MyProfileActivity, "Updated sucessfully", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this@MyProfileActivity, it.exception!!.message, Toast.LENGTH_SHORT).show()

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
//        Log.d(TAG, "storeData: ${auth.uid.toString()}")


    }

    companion object{
        const val TAG="JONHDOE"
    }
}