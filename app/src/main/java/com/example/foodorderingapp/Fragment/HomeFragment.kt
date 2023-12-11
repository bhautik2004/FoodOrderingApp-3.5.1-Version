package com.example.foodorderingapp.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.Adapter.FoodItemAdapter
import com.example.foodorderingapp.Adapter.ItemAdapter
import com.example.foodorderingapp.DatabaseHelper
import com.example.foodorderingapp.R

class HomeFragment : Fragment() {

    lateinit var dbHelper: DatabaseHelper
    private lateinit var foodItemAdapter: FoodItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.menuRv)

        dbHelper = DatabaseHelper(requireContext())
        val foodItemList = dbHelper.getAllFoodItem()
        foodItemAdapter = FoodItemAdapter(requireContext(), foodItemList)

        recyclerView.adapter = foodItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        return view
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}

