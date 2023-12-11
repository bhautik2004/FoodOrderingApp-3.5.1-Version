package com.example.foodorderingapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_food_details.*
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_detail.backButton
import kotlinx.android.synthetic.main.activity_item_detail.fooddescription
import kotlinx.android.synthetic.main.activity_item_detail.foodimage
import kotlinx.android.synthetic.main.activity_item_detail.foodname
import kotlinx.android.synthetic.main.activity_item_detail.foodprice
import kotlinx.android.synthetic.main.popularitem.*

class FoodDetailsActivity : AppCompatActivity() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"
    private var dbHelper:DatabaseHelper? = null
    private var recordId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)
        backButton.setOnClickListener{
            finish()
        }
        dbHelper = DatabaseHelper(this)
        val intent = intent
        recordId = intent.getStringExtra("Item_Id")
        showItemDetails()
        addToCartbtn.setOnClickListener {
            addToCart()
        }

    }
    private fun showItemDetails() {
        val selectQry = "SELECT * FROM food_items WHERE id =\"$recordId\""
        val db = dbHelper!!.writableDatabase
        val cursor = db.rawQuery(selectQry, null)

        if (cursor.moveToFirst()) {
            do {
                val id = "" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_COLUMN))
                val foodName = "" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_NAME_COLUMN))
                val foodPrice = "" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_PRICE_COLUMN))
                val foodImage = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.FOOD_IMAGE_COLUMN))
                val foodDescription = "" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.FOOD_DESCRIPTION_COLUMN))

                foodname.text = foodName
                foodprice.text = foodPrice
                fooddescription.text = foodDescription

                if (foodImage != null) {
                    val bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.size)
                    foodimage.setImageBitmap(bitmap)
                } else {
                    foodimage.setImageResource(R.drawable.burrgar)
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }
    private fun addToCart() {
        val dbh = DatabaseHelper(this)
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val userEmail: String = sharedPreferences.getString("user_email", null).toString()
        val db = dbHelper!!.writableDatabase
        val user_id: Long = dbh.getUserIdByEmail(userEmail)
        val selectQry = "SELECT * FROM food_items WHERE id =\"$recordId\""
        val cursor = db.rawQuery(selectQry, null)

        if (cursor.moveToFirst()) {
            do {
                val foodId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_COLUMN))
                val success: Long = dbh.addToCart(user_id, foodId, 1)

                if (success != -1L) {
                    Toast.makeText(this, "Item Added To Cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Item is already in the cart", Toast.LENGTH_SHORT).show()
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }

}
