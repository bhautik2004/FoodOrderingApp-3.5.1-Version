package com.example.foodorderingapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.foodorderingapp.model.CartItem
import kotlinx.android.synthetic.main.activity_all_item.*
import kotlinx.android.synthetic.main.activity_all_item.backButton
import kotlinx.android.synthetic.main.activity_order_details.*
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        backButton.setOnClickListener {
            finish()
        }

        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val user_email = sharedPreferences.getString("user_email", null).toString()

        val db = DatabaseHelper(this)
        val userId = db.getUserIdByEmail(user_email)
        val userName: String = db.getUserNameByEmail(user_email).toString()

        val username = findViewById<EditText>(R.id.username)
        username.setText(userName)

        val useremail = findViewById<EditText>(R.id.useremail)
        useremail.setText(user_email)


        val cartItems = db.getCartItems(userId)
        val totalAmount = calculateTotalAmount(cartItems).toInt()
        val formattedTotalAmount = "₹ $totalAmount"
        val totalAmountEditText = findViewById<EditText>(R.id.totalAmountEditText)
        totalAmountEditText.setText(formattedTotalAmount)

        val pleaceorderbtn = findViewById<Button>(R.id.placeorderbtn)
        pleaceorderbtn.setOnClickListener {
            val userphone = userphone.text.toString()
            val useraddress = useraddress.text.toString()

            if (totalAmount < 1) {
                Toast.makeText(this, "Cart Is Empty ", Toast.LENGTH_SHORT).show()
            } else if (userphone.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show()
            } else if (!isValidPhoneNumber(userphone)) {
                Toast.makeText(this, "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show()
            } else if (useraddress.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show()
            } else {
                val currentDate = getCurrentDate()
                val orderID = db.placeorder(
                    userName,
                    user_email,
                    userphone,
                    useraddress,
                    currentDate,
                    totalAmount.toString()
                )
                Log.d("OrderDetailsActivity", "Order ID: $orderID")
                if (orderID > 0) {
                        val success = db.updateOrderIdInCartForUser(userId, orderID)
                        if (success) {
                            Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, CongratsActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed To Placed Order", Toast.LENGTH_SHORT)
                                .show()
                        }

                } else {
                    Toast.makeText(this, "Failed To Placed Order", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 10
    }

    private fun calculateTotalAmount(cartItems: List<CartItem>): Double {
        var totalAmount = 0.0
        for (item in cartItems) {
            val price = item.foodPrice.toDouble()
            val quantity = item.quantity
            totalAmount += price * quantity
        }
        return totalAmount
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

}