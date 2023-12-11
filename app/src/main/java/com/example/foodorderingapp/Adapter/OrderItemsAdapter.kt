package com.example.foodorderingapp.Adapter
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.R
import com.example.foodorderingapp.model.CartItem
import kotlinx.android.synthetic.main.item_order.view.*


class OrderItemsAdapter(private val orderItems: List<CartItem>) :
    RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder>() {

    class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val orderItem = orderItems[position]

        holder.itemView.textFoodName.text = orderItem.foodName
        holder.itemView.textFoodPrice.text = "Price: ₹${orderItem.foodPrice}"
        holder.itemView.textQuantity.text = "Quantity: ${orderItem.quantity}"
        val imageBitmap = BitmapFactory.decodeByteArray(orderItem.foodImage, 0, orderItem.foodImage?.size ?: 0)
        holder.itemView.imageFood.setImageBitmap(imageBitmap)
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }
}
