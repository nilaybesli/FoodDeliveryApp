package com.duyguorhan.yemeksiparisi

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.duyguorhan.yemeksiparisi.databinding.ActivityCustomerLoginBinding
import com.duyguorhan.yemeksiparisi.databinding.ActivityRestaurantLoginBinding
import com.google.firebase.auth.FirebaseAuth

class RestaurantLogin : AppCompatActivity() {
    //viewbinding
    private lateinit var binding: ActivityRestaurantLoginBinding

    //actionbar
    private lateinit var actionBar: ActionBar
    //Progress dialog
    private lateinit var progressDialog: ProgressDialog
    // firebaseauth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_login)

        val ButtonRestaurantLogin = findViewById<Button>(R.id.RestaurantLoginBtn)
        ButtonRestaurantLogin.setOnClickListener {

            val intent = Intent(this, RestaurantMainActivity::class.java)
            startActivity(intent)
        }
        val ButtonRestaurantRegister = findViewById<Button>(R.id.RestaurantSignUp)
        ButtonRestaurantRegister.setOnClickListener {
            val intent = Intent(this, RestaurantRegister::class.java)
            startActivity(intent)

            binding = ActivityRestaurantLoginBinding.inflate(layoutInflater)

            setContentView(binding.root)

            //configure actionbar
            actionBar = supportActionBar!!
            actionBar.title = "Login"

            //configure progress dialog
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Please wait")
            progressDialog.setMessage("Logging In...")
            progressDialog.setCanceledOnTouchOutside(false)

            // init firebaseAuth
            firebaseAuth = FirebaseAuth.getInstance()
            checkUser();

            //handle click , signupactivity

            binding.noAccountTv.setOnClickListener {
                startActivity(Intent(this, RestaurantRegister::class.java))
            }


            //handle click, begin login
            binding.RestaurantLoginBtn.setOnClickListener {
                //before logging in, validate data
                validateData()
            }


        }
    }
    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            //no password entered
            binding.passwordEt.error = "Please enter password"

        } else {
            //data is validated, begin login
            firebaseLogin()


        }
    }

    private fun firebaseLogin() {
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login succes
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT).show()


                //open profile
                startActivity(Intent(this, CustomerMainActivity::class.java))
                finish()

            }
            .addOnFailureListener { e ->
                //login fail
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private fun checkUser() {
        //if user is already logged in go to profile activity
        // get current user
        val firebaseUser= firebaseAuth.currentUser
        if(firebaseUser!= null){
            // user is logged in
            startActivity(Intent(this,CustomerMainActivity::class.java))
            finish()
        }
    }



}
