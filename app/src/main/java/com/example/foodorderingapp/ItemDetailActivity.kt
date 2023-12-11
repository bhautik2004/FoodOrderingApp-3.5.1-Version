package com.example.foodorderingapp
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_detail.backButton

class ItemDetailActivity : AppCompatActivity() {

    private var dbHelper:DatabaseHelper? = null
    private var recordId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        backButton.setOnClickListener{
            finish()
        }
        dbHelper = DatabaseHelper(this)
        val intent = intent
        recordId = intent.getStringExtra("Item_Id")
        showItemDetails()
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
                foodprice.text = "₹ ${foodPrice}"
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

}
