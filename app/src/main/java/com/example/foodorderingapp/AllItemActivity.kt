package com.example.foodorderingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.Adapter.ItemAdapter
import kotlinx.android.synthetic.main.activity_add_item2.backButton
import kotlinx.android.synthetic.main.activity_all_item.*

class AllItemActivity : AppCompatActivity(), ItemAdapter.OnItemDeletedListener {
    override fun onItemDeleted() {
        loadItems()
    }


    lateinit var dbHelper: DatabaseHelper
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_item)

        dbHelper = DatabaseHelper(this)
        loadItems()
        backButton.setOnClickListener{
            finish()
        }
    }

    private fun loadItems() {
//        val adapterItem = ItemAdapter(this,dbHelper.getAllFoodItem())
//        allitemRv.adapter = adapterItem

        val foodList = dbHelper.getAllFoodItem()

        itemAdapter = ItemAdapter(this,foodList,this)
        allitemRv.adapter = itemAdapter
        allitemRv.layoutManager = LinearLayoutManager(this)
        allitemRv.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }
}
