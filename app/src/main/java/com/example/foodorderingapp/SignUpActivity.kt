package com.example.foodorderingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        loginlink.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        createadminuserButton.setOnClickListener {
            val signupName = userName.text.toString()
            val signupEmail = userEmail.text.toString()
            val signupPassword = userPassword.text.toString()
              if(signupName.isEmpty() || signupEmail.isEmpty() || signupPassword.isEmpty()){
                  Toast.makeText(this,"Please Fill All Fields",Toast.LENGTH_SHORT).show()
            }else if (!isValidEmail(signupEmail)) {
                  Toast.makeText(this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show()
              } else{
                  signupDatabase(signupName,signupEmail,signupPassword)
            }
        }

    }
    val databaseHelper = DatabaseHelper(this)
    private fun signupDatabase(name: String, email: String, password: String) {
        try {
            val insertedRowId = databaseHelper.signup(name, email, password)
            if (insertedRowId != -1L) {
                Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "SignUp Failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: DatabaseHelper.UserAlreadyExistsException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
