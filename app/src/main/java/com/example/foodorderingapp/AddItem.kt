package com.example.foodorderingapp

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import java.io.ByteArrayOutputStream


class AddItem : AppCompatActivity() {
    private var isEditMode = false
    private lateinit var db: DatabaseHelper
    private var fid: String? = null
    private var foodname: String? = null
    private var foodprice: String? = null
    private val foodimage: Uri? = null
    private var fooddescription: String? = null
    internal var SELECT_PICTURE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item2)

        db = DatabaseHelper(this)
        val helper = DatabaseHelper(this)
        val FoodName = findViewById<View>(R.id.enterFoodName) as EditText
        val FoodPrice = findViewById<View>(R.id.enterFoodPrice) as EditText
        val selectedImage = findViewById<View>(R.id.selectedImage) as ImageView
        val foodDescription = findViewById<View>(R.id.foodDescription) as EditText
        val selectButton = findViewById<View>(R.id.selectImage) as TextView
        val addItemButton = findViewById<View>(R.id.addItemButton) as Button
        val backButton = findViewById<View>(R.id.backButton) as ImageButton
        //get data from intent
        val i = intent
        isEditMode = i.getBooleanExtra("isEditMode", false)
        if (isEditMode) {
            val title = findViewById<View>(R.id.title) as TextView
            addItemButton.text = "Update Item"
            title.text = "Update Item"
            fid = i.getStringExtra("id")
            foodname = i.getStringExtra("foodName")
            foodprice = i.getStringExtra("foodPrice")
            fooddescription = i.getStringExtra("foodDescription")
            val foodImageString = i.getStringExtra("foodImage")
            if (foodImageString != null) {
                val decodedString =
                    android.util.Base64.decode(foodImageString, android.util.Base64.DEFAULT)
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                if (decodedByte != null) {
                    selectedImage.setImageBitmap(decodedByte)
                } else {
                    selectedImage.setImageResource(R.drawable.burrgar)
                }
            } else {
                selectedImage.setImageResource(R.drawable.burrgar)
            }

            FoodName.setText(foodname)
            FoodPrice.setText(foodprice)
            foodDescription.setText(fooddescription)


        }
        backButton.setOnClickListener { finish() }


        selectButton.setOnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
        }

        addItemButton.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View) {


                if (FoodName.text.length == 0 || FoodPrice.text.length == 0 || foodDescription.text.length == 0) {
                    Toast.makeText(this@AddItem, "Please Fill All Fields", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (isEditMode) {
                        val imageByteArray = ImageViewToByte(selectedImage)
                        db.updateFoodItem(
                            fid!!,
                            FoodName.text.toString(),
                            Integer.parseInt(FoodPrice.text.toString()),
                            imageByteArray,
                            foodDescription.text.toString()
                        )
                        Toast.makeText(this@AddItem, "Item Updated...", Toast.LENGTH_SHORT).show()
                    } else {
                        val imageByteArray = ImageViewToByte(selectedImage)
                        val success = db.insertFoodItem(
                            FoodName.text.toString(),
                            Integer.parseInt(FoodPrice.text.toString()),
                            imageByteArray,
                            foodDescription.text.toString()
                        )
                        if (success) {
                            Toast.makeText(this@AddItem, "Item Inserted", Toast.LENGTH_SHORT).show()
                            selectedImage.setImageResource(R.drawable.addimage)
                            foodDescription.setText("")
                            FoodName.setText("")
                            FoodPrice.setText("")
                        } else {
                            Toast.makeText(
                                this@AddItem,
                                "Failed to insert item",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            private fun ImageViewToByte(selectedImage: ImageView): ByteArray {
                val bitmap = (selectedImage.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                return stream.toByteArray()
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val selectimage = findViewById<ImageView>(R.id.selectedImage)
        super@AddItem.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data!!.data
                if (null != selectedImageUri) {
                    selectimage.setImageURI(selectedImageUri)
                }
            }
        }
    }

}
