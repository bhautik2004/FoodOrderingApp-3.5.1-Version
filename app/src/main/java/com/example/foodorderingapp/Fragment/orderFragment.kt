package com.example.foodorderingapp.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.Adapter.MyOrderAdapter
import com.example.foodorderingapp.DatabaseHelper
import com.example.foodorderingapp.R


class orderFragment : Fragment() {
    private val SHARED_PREF_NAME = "foodorderingapp_shared_pref"

    private lateinit var myOrderAdapter: MyOrderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        databaseHelper = DatabaseHelper(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val userEmail = sharedPreferences.getString("user_email", null)
        val orderList = databaseHelper.getFoodOrderDetailsForUser(userEmail.toString())

        myOrderAdapter = MyOrderAdapter(orderList)
        recyclerView.adapter = myOrderAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
