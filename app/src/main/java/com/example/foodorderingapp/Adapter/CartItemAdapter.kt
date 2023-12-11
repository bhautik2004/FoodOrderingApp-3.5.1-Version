import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.Adapter.ItemAdapter
import com.example.foodorderingapp.DatabaseHelper
import com.example.foodorderingapp.R
import com.example.foodorderingapp.model.CartItem

class CartItemAdapter(
    private val context: Context,
    private val cartItemList: ArrayList<CartItem>,
    private val itemDeletedListener: OnItemDeletedListener
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"

    var dbHelper: DatabaseHelper? = null

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartfoodImage: ImageView = itemView.findViewById(R.id.cartfoodImage)
        val cartfoodName: TextView = itemView.findViewById(R.id.cartfoodName)
        var cartitemPrice: TextView = itemView.findViewById(R.id.cartfitemPrice)
        var quantity: TextView = itemView.findViewById(R.id.quantity)
        val plusbtn: ImageButton = itemView.findViewById(R.id.plusbtn)
        val minusbtn: ImageButton = itemView.findViewById(R.id.minusbtn)
        val deletebtn: ImageButton = itemView.findViewById(R.id.deletebtn)
        var originalPrice: Double = 0.0
    }

    interface OnItemDeletedListener {
        fun onItemDeleted()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = cartItemList[position]

        val bitmap =
            BitmapFactory.decodeByteArray(currentItem.foodImage, 0, currentItem.foodImage.size)
        holder.cartfoodImage.setImageBitmap(bitmap)
        holder.cartfoodName.text = currentItem.foodName
        holder.cartitemPrice.text = currentItem.foodPrice
        holder.quantity.text = currentItem.quantity.toString()

        if (holder.originalPrice == 0.0) {
            holder.originalPrice = currentItem.foodPrice.toDouble()
        }

        holder.deletebtn.setOnClickListener {
            showDeleteConfirmationDialog(currentItem)
        }

        holder.plusbtn.setOnClickListener {
            currentItem.quantity++
            holder.quantity.text = currentItem.quantity.toString()
            updateItemPrice(holder, currentItem)
            updateQuantityInDatabase(currentItem)
        }

        holder.minusbtn.setOnClickListener {
            if (currentItem.quantity > 1) {
                currentItem.quantity--
                holder.quantity.text = currentItem.quantity.toString()
                updateItemPrice(holder, currentItem)
                updateQuantityInDatabase(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    private fun showDeleteConfirmationDialog(currentItem: CartItem) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this item?")

        val positiveClickListener = DialogInterface.OnClickListener { _, _ ->
            dbHelper = DatabaseHelper(context)
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("user_email", null)
            val userId = dbHelper!!.getUserIdByEmail(userEmail.toString())
            dbHelper!!.deleteCartItem(userId, currentItem.id)
            itemDeletedListener.onItemDeleted()
        }
        val negativeClickListener = DialogInterface.OnClickListener { _, _ ->

        }
        builder.setPositiveButton("Yes", positiveClickListener)
        builder.setNegativeButton("No", negativeClickListener)
        builder.show()
    }

    private fun updateItemPrice(holder: CartItemViewHolder, currentItem: CartItem) {
        val updatedPrice = holder.originalPrice * currentItem.quantity
        currentItem.foodPrice = updatedPrice.toInt().toString()
        holder.cartitemPrice.text = currentItem.foodPrice
    }
    private fun updateQuantityInDatabase(currentItem: CartItem) {
        dbHelper = DatabaseHelper(context)
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)
        val userId = dbHelper!!.getUserIdByEmail(userEmail.toString())
        dbHelper!!.updateCartItemQuantity(userId, currentItem.id, currentItem.quantity)
    }
}
