package com.example.foodorderingapp.Fragment
import CartItemAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.DatabaseHelper
import com.example.foodorderingapp.OrderDetailsActivity

import com.example.foodorderingapp.R


class CartFragment : Fragment(), CartItemAdapter.OnItemDeletedListener  {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"

    override fun onItemDeleted() {
        loadItems()
    }

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartItemAdapter: CartItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val proceedbtn: Button = view.findViewById(R.id.proceedbtn)
        cartRecyclerView = view.findViewById(R.id.cartRV)
        loadItems()
        proceedbtn.setOnClickListener {
            val intent  = Intent(context, OrderDetailsActivity::class.java)
            startActivity(intent)

        }
        return view
    }
    private fun loadItems() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)
        val dbHelper = DatabaseHelper(requireContext())
        val userid = dbHelper.getUserIdByEmail(userEmail.toString())
        val cartItemList = dbHelper.getCartItems(userid)
        cartItemAdapter = CartItemAdapter(requireContext(), cartItemList,this)
        cartItemAdapter = CartItemAdapter(requireContext(), cartItemList,this)
        cartRecyclerView.adapter = cartItemAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}