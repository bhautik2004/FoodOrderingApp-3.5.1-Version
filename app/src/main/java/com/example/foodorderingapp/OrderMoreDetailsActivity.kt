package com.example.foodorderingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.Adapter.OrderItemsAdapter
import kotlinx.android.synthetic.main.activity_admin_profile.*
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_order_more_details.*
import kotlinx.android.synthetic.main.activity_order_more_details.backButton
import kotlinx.android.synthetic.main.orders_sample.*
import kotlinx.android.synthetic.main.orders_sample.textTotalAmount
import kotlinx.android.synthetic.main.orders_sample.textUsername

class OrderMoreDetailsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_more_details)
        backButton.setOnClickListener {
            val intent = Intent(this,OrdersActivity::class.java)
            startActivity(intent)
            finish()
        }

        val db = DatabaseHelper(this)
        val orderId = intent.getLongExtra("orderid",1L)

        if (orderId != null) {

            val orderDetails = db.getOrderDetails(orderId)
            Log.d("OrderDetails", orderDetails.toString())
            if (orderDetails != null) {
                textOrderId.text = " ${orderDetails.orderId}"
                textUsername.text = orderDetails.username
                textTotalAmount.text = orderDetails.totalAmount
                textDate.text = orderDetails.date
                textAddress.text = orderDetails.address
                textPhone.text = orderDetails.phone
            }
        }
        val orderItems = db.getOrderedItems(orderId)
        val orderItemsAdapter = OrderItemsAdapter(orderItems)
        recyclerViewOrderItems.layoutManager = LinearLayoutManager(this)
        recyclerViewOrderItems.adapter = orderItemsAdapter

        val deliveredbtn = findViewById<Button>(R.id.deliveredbtn)
        deliveredbtn.setOnClickListener {
            if (orderId > 0) {
                db.deleteOrder(orderId)
                Toast.makeText(this, "Order Delivered Successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this,OrdersActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "No Order Found", Toast.LENGTH_SHORT).show()
            }
        }
    }

}