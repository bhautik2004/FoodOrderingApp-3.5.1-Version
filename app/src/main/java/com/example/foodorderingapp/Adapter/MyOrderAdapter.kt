package com.example.foodorderingapp.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.R
import com.example.foodorderingapp.model.MyOrderDetails

class MyOrderAdapter(private val orderList: List<MyOrderDetails>) :
    RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodNameTextView: TextView = itemView.findViewById(R.id.foodNameTextView)
        val foodPriceTextView: TextView = itemView.findViewById(R.id.foodpriseTextView)
        val foodQuantityTextView: TextView = itemView.findViewById(R.id.foodQuantityTextView)
        val orderDateTimeTextView: TextView = itemView.findViewById(R.id.orderDateTimeTextView)
        val foodImageView: ImageView = itemView.findViewById(R.id.foodPhotoImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myorder_sample, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderDetails = orderList[position]

        with(holder) {
            foodNameTextView.text = orderDetails.foodName
            foodPriceTextView.text = orderDetails.foodPrice
            foodQuantityTextView.text = orderDetails.foodQuantity.toString()
            orderDateTimeTextView.text = orderDetails.orderDateTime
            val bitmap = BitmapFactory.decodeByteArray(orderDetails.foodImage, 0, orderDetails.foodImage.size)
            foodImageView.setImageBitmap(bitmap)

        }
    }

    override fun getItemCount(): Int = orderList.size
}
