package com.example.foodorderingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_item2.*
import kotlinx.android.synthetic.main.fragment_profile.*

class AdminProfileActivity : AppCompatActivity() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)
        backButton.setOnClickListener {
            finish()
        }
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val adminEmail = sharedPreferences?.getString("admin_email", "")
        val databaseHelper = DatabaseHelper(this)
        val userName = databaseHelper.getAdminNameByEmail(adminEmail ?: "");

        nameTextView.text = userName
        emailTextView.text = adminEmail
        logoutButton.setOnClickListener {
            sharedPreferences?.edit()?.apply {
                clear()
                apply()
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}
