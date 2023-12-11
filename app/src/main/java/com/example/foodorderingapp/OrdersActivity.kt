package com.example.foodorderingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.Adapter.OrdersAdapter
import kotlinx.android.synthetic.main.activity_add_item2.*

class OrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        backButton.setOnClickListener {
            finish()
        }
        val ordersList = DatabaseHelper(this).getAllOrders()

        val recyclerView: RecyclerView = findViewById(R.id.ordersRV)
        val adapter = OrdersAdapter(this,ordersList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }
}
