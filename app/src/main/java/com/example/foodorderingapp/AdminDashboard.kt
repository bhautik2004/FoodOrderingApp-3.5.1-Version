package com.example.foodorderingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_admin_dashboard.*

class AdminDashboard : AppCompatActivity() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        setContentView(R.layout.activity_admin_dashboard)
        addMenu.setOnClickListener{
            val intent = Intent(this,AddItem::class.java)
            intent.putExtra("isEditMode",false)
            startActivity(intent)
        }
        allItemList.setOnClickListener {
            val intent = Intent(this,AllItemActivity::class.java)
            startActivity(intent)
        }
        createuser.setOnClickListener {
            val intent = Intent(this,AddAdminUserActivity::class.java)
            startActivity(intent)
        }
        profile.setOnClickListener {
            val intent = Intent(this,AdminProfileActivity::class.java)
            startActivity(intent)
        }
        orders.setOnClickListener{
            val intent = Intent(this,OrdersActivity::class.java)
            startActivity(intent)
        }
        allUserList.setOnClickListener {
            val intent = Intent(this,AllUserListActivity::class.java)
            startActivity(intent)
        }
        logoutButton.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent  = Intent(this,AdminLogin::class.java)
            startActivity(intent)
            finish()
        }

    }
}
