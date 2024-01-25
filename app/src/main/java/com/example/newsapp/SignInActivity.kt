package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import com.example.newsapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()


        val username = binding.txtInputEditextUsername
        val email = binding.txtInputEditextEmail
        val password = binding.txtInputPassword
        binding.txtForgotPassword.setOnClickListener {
            val intent= Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        //username
        binding.txtInputEditextUsername.hint="Username"
        binding.txtInputEditextUsername.setOnFocusChangeListener(object : OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus){
                    binding.txtInputEditextUsername.hint = ""
                    binding.txtUsername.visibility=View.VISIBLE
                }else if (!hasFocus&&binding.txtInputEditextUsername.text.toString().length>1){
                    binding.txtInputEditextUsername.hint =""
                    binding.txtUsername.visibility=View.VISIBLE
                }
                else{
                    binding.txtInputEditextUsername.hint ="Username"
                    binding.txtUsername.visibility=View.INVISIBLE
                }

            }

        })
        //email
        binding.txtInputEditextEmail.hint="Email"
        binding.txtInputEditextEmail.setOnFocusChangeListener(object : OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus){
                    binding.txtInputEditextEmail.hint = ""
                    binding.txtEmail.visibility=View.VISIBLE
                }else if (!hasFocus&&binding.txtInputEditextEmail.text.toString().length>1){
                    binding.txtInputEditextEmail.hint =""
                    binding.txtEmail.visibility=View.VISIBLE
                }
                else{
                    binding.txtInputEditextEmail.hint ="Email"
                    binding.txtEmail.visibility=View.INVISIBLE
                }
            }

        })
        //password
        binding.txtInputPassword.hint="Password"
        binding.txtInputPassword.setOnFocusChangeListener(object : OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus){
                    binding.txtInputPassword.hint = ""
                    binding.txtPassword.visibility=View.VISIBLE
                }else if (!hasFocus&&binding.txtInputPassword.text.toString().length>1){
                    binding.txtInputPassword.hint =""
                    binding.txtPassword.visibility=View.VISIBLE
                }
                else{
                    binding.txtInputPassword.hint ="Password"
                    binding.txtPassword.visibility=View.INVISIBLE
                }
            }

        })


        binding.textRegister.setOnClickListener {
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignIn.setOnClickListener {
            val userName = username.text.toString()
            val Email = email.text.toString()
            val password = password.text.toString()

            if (binding.txtInputEditextUsername.text.toString().isEmpty()
                &&binding.txtInputEditextEmail.text.toString().isEmpty()
                &&binding.txtInputPassword.text.toString().isEmpty()){
                binding.txtUsrNameRequired.visibility=View.INVISIBLE
                binding.txtUsrEmailRequired.visibility=View.INVISIBLE
                binding.txtUsrPassRequired.visibility=View.INVISIBLE
            }
            if (userName.isEmpty()){
                binding.txtUsrNameRequired.visibility=View.VISIBLE
            }
            if (Email.isEmpty()){
                binding.txtUsrEmailRequired.visibility=View.VISIBLE
            }
            if (binding.txtInputPassword.text?.isEmpty() == true){
                binding.txtUsrPassRequired.visibility=View.VISIBLE
            }

            if (username != null && email != null  && password != null) {

                if (userName.isEmpty() || Email.isEmpty() || password.isEmpty()) {

                    Toast.makeText(this, "Fill all the credentials", Toast.LENGTH_SHORT).show()
                }else if (password.length<6){
                    Toast.makeText(this, "password is too short", Toast.LENGTH_SHORT).show()
                }else{
                    signInUser(Email,password)
                }

            }
        }
    }

    private fun signInUser(Email: String, password: String) {
        auth.signInWithEmailAndPassword(Email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent=Intent(this,SelectYourCityActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }

    }
    companion object{
        const val TAG="SIGNINACTIVITY"
    }
}