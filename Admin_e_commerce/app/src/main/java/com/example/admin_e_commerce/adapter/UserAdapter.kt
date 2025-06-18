package com.example.admin_e_commerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_e_commerce.R
import com.example.admin_e_commerce.model.UserModel

class UserAdapter(private val userList: List<UserModel>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUid: TextView = itemView.findViewById(R.id.tvUid)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvUid.text = "${user.uid}"
        holder.tvAddress.text = "${user.userAddress}"
        holder.tvPhone.text = " ${user.userPhoneNumber}"
    }

    override fun getItemCount(): Int = userList.size
}
