package com.example.foodorderingapp

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.Adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_admin_profile.*

class AllUserListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: Button
    lateinit var dbh:DatabaseHelper
    private lateinit var newArray: ArrayList<Userlist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user_list)
        backButton.setOnClickListener {
            finish()
        }
        dbh = DatabaseHelper(this)
        recyclerView = findViewById(R.id.alluserrecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        displayuser()
    }
    private fun displayuser(){
        var newcursor :Cursor? = dbh.getUser()
        newArray = ArrayList<Userlist>()
        while (newcursor!!.moveToNext()){
            val uname = newcursor.getString(1)
            val uemail = newcursor.getString(2)
            newArray.add(Userlist(uname,uemail))
        }
        recyclerView.adapter = UserAdapter(newArray)
    }
}
