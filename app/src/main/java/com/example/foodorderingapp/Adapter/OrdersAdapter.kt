package com.example.foodorderingapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.OrderMoreDetailsActivity
import com.example.foodorderingapp.R
import com.example.foodorderingapp.model.Order

class OrdersAdapter(private val activity: AppCompatActivity, private val orderList: ArrayList<Order>) :
    RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textUsername: TextView = itemView.findViewById(R.id.textUsername)
        val textTotalAmount: TextView = itemView.findViewById(R.id.textTotalAmount)
        val btnMoreDetails:Button = itemView.findViewById(R.id.btnMoreDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.orders_sample, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentOrder = orderList[position]

        holder.textUsername.text = currentOrder.username
        holder.textTotalAmount.text = currentOrder.totalAmount

        holder.btnMoreDetails.setOnClickListener {
            val intent = Intent(activity,OrderMoreDetailsActivity::class.java)
            intent.putExtra("orderid",currentOrder.orderId)
            activity.startActivity(intent)
            activity.finish()
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}