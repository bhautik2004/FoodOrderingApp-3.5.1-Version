package com.example.foodorderingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin_login.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.adminEmail
import kotlinx.android.synthetic.main.activity_login.adminPassword

class AdminLogin : AppCompatActivity() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val admin_Email = sharedPreferences.getString("admin_email", null)
        if (admin_Email != null) {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish()
        } else {

        setContentView(R.layout.activity_admin_login)
        adminloginbtn.setOnClickListener {
            val adminemail = adminEmail.text.toString()
            val adminpassword = adminPassword.text.toString()
            if (adminemail.isEmpty() || adminpassword.isEmpty()) {
                Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(adminemail)) {
                Toast.makeText(this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show()
            } else {
                loginDatabase(adminemail, adminpassword)            }
        }
        }
    }
    val databaseHelper = DatabaseHelper(this)
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun loginDatabase(email: String, password: String) {
        val isUser = databaseHelper.adminlogin(email, password)
        if (isUser) {
            saveUserDetails(email)
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminDashboard::class.java)
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
        editor.putString("admin_email", email)
        editor.apply()
    }
}
