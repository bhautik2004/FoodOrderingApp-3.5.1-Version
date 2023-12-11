package com.example.foodorderingapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.*
import com.example.foodorderingapp.model.FoodItem
class ItemAdapter(private val context: Context, private val foodList: List<FoodItem>,  private val itemDeletedListener: OnItemDeletedListener) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var dbHelper:DatabaseHelper ?= null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val foodDescription: TextView = itemView.findViewById(R.id.foodDescription)
        var foodEdtBtn:ImageView = itemView.findViewById(R.id.foodEdtBtn)
        var foodDltBtn:ImageView = itemView.findViewById(R.id.foodDltBtn)
    }

    interface OnItemDeletedListener {
            fun onItemDeleted()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = foodList[position]

        holder.foodName.text = currentItem.foodName
        holder.foodPrice.text = currentItem.foodPrice

        holder.foodDescription.text = currentItem.foodDescription
        val bitmap =
            BitmapFactory.decodeByteArray(currentItem.foodImage, 0, currentItem.foodImage.size)
        holder.foodImage.setImageBitmap(bitmap)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemDetailActivity::class.java)
            intent.putExtra("Item_Id", currentItem.id)
            context.startActivity(intent)
        }
        holder.foodEdtBtn.setOnClickListener {
            val intent = Intent(context,AddItem::class.java)
            intent.putExtra("id",currentItem.id)
            intent.putExtra("foodName",currentItem.foodName)
            intent.putExtra("foodPrice",currentItem.foodPrice)
            val baseImage = android.util.Base64.encodeToString(currentItem.foodImage, android.util.Base64.DEFAULT)
            intent.putExtra("foodImage", baseImage)
            intent.putExtra("foodDescription",currentItem.foodDescription)
            intent.putExtra("isEditMode",true)
            context.startActivity(intent)
        }
        holder.foodDltBtn.setOnClickListener {
            showDeleteConfirmationDialog(currentItem)
        }


    }
    override fun getItemCount(): Int {
        return foodList.size
    }
    private fun showDeleteConfirmationDialog(currentItem: FoodItem) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this item?")

        val positiveClickListener = DialogInterface.OnClickListener { _, _ ->
            dbHelper = DatabaseHelper(context)
            dbHelper!!.deleteFoodItem(currentItem.id)
            itemDeletedListener.onItemDeleted()
        }

        val negativeClickListener = DialogInterface.OnClickListener { _, _ ->

        }

        builder.setPositiveButton("Yes", positiveClickListener)
        builder.setNegativeButton("No", negativeClickListener)

        builder.show()
    }
}



//    private var contex:Context?=null
//    private var itemList:ArrayList<FoodItem>?=null
//
//    constructor(context: Context?,itemList:ArrayList<FoodItem>):this(){
//        this.contex = contex
//        this.itemList = itemList
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderItem {
//
//        return HolderItem(
//            LayoutInflater.from(contex).inflate(R.layout.activity_all_item,parent,false)
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return itemList!!.size
//    }
//
//    override fun onBindViewHolder(holder: HolderItem, position: Int) {
//        val model = itemList!!.get(position)
//        val id = model.id
//        val foodName = model.foodName
//        val foodPrice = model.foodPrice
//        val foodImage = model.foodImage
//        val foodDescription = model.foodDescription
//
//        holder.foodName.text = foodName
//        holder.foodPrice.text = foodPrice.toString()
//        holder.foodDescription.text = foodDescription
//        if (foodImage == null){
//            holder.foodimage.setImageResource(R.drawable.burrgar)
//        }else{
//            holder.foodimage.setImageURI(Uri.parse(foodImage))
//        }
//        holder.itemView.setOnClickListener{
//            val intent = Intent(contex , ItemDetailActivity::class.java)
//            intent.putExtra("Item_Id",id)
//            contex?.startActivity(intent)
//        }
//        holder.foodEdtBtn.setOnClickListener{
//
//        }
//        holder.foodDltBtn.setOnClickListener{
//
//        }
//    }
//
//    inner class HolderItem(itemView: View): RecyclerView.ViewHolder(itemView) {
//
//        var foodimage:ImageView = itemView.findViewById(R.id.foodImage)
//        var foodName:TextView = itemView.findViewById(R.id.foodName)
//        var foodDescription:TextView = itemView.findViewById(R.id.foodDescription)
//        var foodPrice:TextView = itemView.findViewById(R.id.foodPrice)
//        var foodEdtBtn:ImageView = itemView.findViewById(R.id.foodEdtBtn)
//        var foodDltBtn:ImageView = itemView.findViewById(R.id.foodDltBtn)
//    }
//}