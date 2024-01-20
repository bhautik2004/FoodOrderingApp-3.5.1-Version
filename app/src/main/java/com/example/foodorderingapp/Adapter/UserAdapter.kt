package com.example.foodorderingapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.R
import com.example.foodorderingapp.Userlist
import kotlinx.android.synthetic.main.activity_splash_screen.*

class UserAdapter(var userList : ArrayList<Userlist>):RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_user_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
       val currentItem = userList[position]
        holder.tname.text = currentItem.name
        holder.temail.text = currentItem.email
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tname : TextView = itemView.findViewById(R.id.uname)
        val temail : TextView = itemView.findViewById(R.id.uemail)
    }

}