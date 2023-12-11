package com.example.foodorderingapp.Adapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.foodorderingapp.DatabaseHelper
import com.example.foodorderingapp.R
import com.example.foodorderingapp.model.FoodItem
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.FoodDetailsActivity
import com.example.foodorderingapp.ItemDetailActivity

class FoodItemAdapter(private val context: Context, private val foodList: List<FoodItem>) :
    RecyclerView.Adapter<FoodItemAdapter.ItemViewHolder>() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"
    var dbHelper: DatabaseHelper?= null

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val foodDescription: TextView = itemView.findViewById(R.id.foodDescription)
        val addToCartBtn:TextView = itemView.findViewById(R.id.addToCart)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = foodList[position]

        holder.foodName.text = currentItem.foodName
        holder.foodPrice.text = currentItem.foodPrice
        holder.foodDescription.text = currentItem.foodDescription

        val bitmap =
            BitmapFactory.decodeByteArray(currentItem.foodImage, 0, currentItem.foodImage.size)
        holder.foodImage.setImageBitmap(bitmap)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FoodDetailsActivity::class.java)
            intent.putExtra("Item_Id", currentItem.id)
            context.startActivity(intent)
        }
        holder.addToCartBtn.setOnClickListener {
            val db = DatabaseHelper(context)
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("user_email", null)

            if (userEmail != null) {
                val user_id: Long = db.getUserIdByEmail(userEmail)

                val addToCartResult: Long = db.addToCart(user_id, Integer.parseInt(currentItem.id), 1)

                if (addToCartResult != -1L) {
                    Toast.makeText(context, "Item Added To Cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Item is already in the cart", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.popularitem, parent, false)

        return ItemViewHolder(itemView)
    }


    override fun getItemCount(): Int {
            return foodList.size
        }
}