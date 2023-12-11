package com.example.foodorderingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)

        if (userEmail != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_login)

            signuplink.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }

            addToCartbtn.setOnClickListener {
                val userEmail = adminEmail.text.toString()
                val userPassword = adminPassword.text.toString()

                if (userEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
                } else if (!isValidEmail(userEmail)) {
                    Toast.makeText(this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show()
                } else {
                    loginDatabase(userEmail, userPassword)
                }
            }

            adminloginlink.setOnClickListener {
                val intent = Intent(this, AdminLogin::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private val databaseHelper = DatabaseHelper(this)

    private fun loginDatabase(email: String, password: String) {
        val isUser = databaseHelper.login(email, password)
        if (isUser) {
            saveUserDetails(email)

            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserDetails(email: String) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.apply()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
