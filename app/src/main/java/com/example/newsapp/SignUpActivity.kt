package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.newsapp.databinding.ActivitySignUpBinding
import com.example.newsapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    //database
    private lateinit var databaseReference: DatabaseReference
    private lateinit var usernameS: String
    private lateinit var phoneNumbers: String
    private lateinit var emailS: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        usernameS = ""
        phoneNumbers = ""
        emailS = ""

        val username = binding.txtInputUsername
        val email = binding.txtInputEmail
        val phoneNumber = binding.txtInputPhone
        val password = binding.txtInputPassword

        //username
        binding.txtInputUsername.hint = "Username"
        binding.txtInputUsername.setOnFocusChangeListener(object :
            View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    binding.txtInputUsername.hint = ""
                    binding.txtUsername.visibility = View.VISIBLE
                } else if (!hasFocus && binding.txtInputUsername.text.toString().length > 1) {
                    binding.txtInputUsername.hint = ""
                    binding.txtUsername.visibility = View.VISIBLE
                } else {
                    binding.txtInputUsername.hint = "Username"
                    binding.txtUsername.visibility = View.INVISIBLE
                }

            }

        })
        //email
        binding.txtInputEmail.hint = "Email"
        binding.txtInputEmail.setOnFocusChangeListener(object :
            View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    binding.txtInputEmail.hint = ""
                    binding.txtEmail.visibility = View.VISIBLE
                } else if (!hasFocus && binding.txtInputUsername.text.toString().length > 1) {
                    binding.txtInputEmail.hint = ""
                    binding.txtEmail.visibility = View.VISIBLE
                } else {
                    binding.txtInputEmail.hint = "Email"
                    binding.txtEmail.visibility = View.INVISIBLE
                }

            }

        })
        //phone
        binding.txtInputPhone.hint = "Phone"
        binding.txtInputPhone.setOnFocusChangeListener(object :
            View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    binding.txtInputPhone.hint = ""
                    binding.txtPhone.visibility = View.VISIBLE
                } else if (!hasFocus && binding.txtInputUsername.text.toString().length > 1) {
                    binding.txtInputPhone.hint = ""
                    binding.txtPhone.visibility = View.VISIBLE
                } else {
                    binding.txtInputPhone.hint = "Phone"
                    binding.txtPhone.visibility = View.INVISIBLE
                }

            }

        })
        //password
        binding.txtInputPassword.hint = "Password"
        binding.txtInputPassword.setOnFocusChangeListener(object :
            View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    binding.txtInputPassword.hint = ""
                    binding.txtPassword.visibility = View.VISIBLE
                } else if (!hasFocus && binding.txtInputPassword.text.toString().length > 1) {
                    binding.txtInputPassword.hint = ""
                    binding.txtPassword.visibility = View.VISIBLE
                } else {
                    binding.txtInputPassword.hint = "Password"
                    binding.txtPassword.visibility = View.INVISIBLE
                }

            }

        })



        binding.gmailSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignUp.setOnClickListener {


            usernameS = username.text.toString()
            emailS = email.text.toString()
            phoneNumbers = phoneNumber.text.toString()
            val passwordS = password.text.toString()
            if (usernameS.isEmpty() && emailS.isEmpty() && phoneNumbers.isEmpty() || passwordS.isEmpty()) {
                binding.txtUsrNameRequired.visibility = View.INVISIBLE
                binding.txtUsrEmailRequired.visibility = View.INVISIBLE
                binding.txtUsrPhoneRequired.visibility = View.INVISIBLE
                binding.txtUsrPassRequired.visibility = View.INVISIBLE
            }
            Log.d("PRABHATJI", "onCreate: $usernameS $emailS $phoneNumbers $passwordS")
            if (usernameS.isEmpty()) {
                binding.txtUsrNameRequired.visibility = View.VISIBLE
            }
            if (emailS.isEmpty()) {
                binding.txtUsrEmailRequired.visibility = View.VISIBLE
            }
            if (phoneNumbers.isEmpty()) {
                binding.txtUsrPhoneRequired.visibility = View.VISIBLE
            }
            if (passwordS.isEmpty()) {
                binding.txtUsrPassRequired.visibility = View.VISIBLE
            }

            if (usernameS.isEmpty() || emailS.isEmpty() || phoneNumbers.isEmpty() || passwordS.isEmpty()) {

                Toast.makeText(this, "Fill all the credentials", Toast.LENGTH_SHORT).show()
            } else if (passwordS.length < 6) {
                Toast.makeText(this, "password is too short", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(
                    binding.txtInputEmail.text.toString(),
                    binding.txtInputPassword.text.toString()
                )
            }

            Log.d("PRABHAT", "onCreate: $username $email  $phoneNumber  $password")

        }


    }

    private fun registerUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("PRABHATJI", "registerUser: $usernameS  $phoneNumbers $emailS")

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    val item = UserModel(
                        uid = auth.uid.toString(),
                        image = "",
                        username = usernameS,
                        phoneNumber = phoneNumbers,
                        firstName = " ",
                        lastName = "",
                        emailId = emailS,
                        userType = "",
                        proofDocument = ""
                    )
                    Log.d(TAG, "registerUser: $item")
                    databaseReference.child(auth.uid.toString()).setValue(item)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data Uploadeed", Toast.LENGTH_SHORT).show()
                            Log.d("PRABHAT", "createUserWithEmail:success")
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                        Toast.makeText(this, "failed to upload", Toast.LENGTH_SHORT).show()
                    }

                    // Sign in success, update UI with the signed-in user's information

//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("PRABHAT", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }

    }

    private fun statusBarColor() {
        val window: Window = this.window
        // Clear FLAG_TRANSLUCENT_STATUS flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

// Add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

// Finally, change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.greeny)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val TAG = "SIGNUPACTIVITYPRABHAT"
    }
}