package com.example.admin_e_commerce.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.admin_e_commerce.databinding.ItemViewImageSelectionBinding

class AdapterSelectedImage(private val imageUris: ArrayList<Uri>) :
    RecyclerView.Adapter<AdapterSelectedImage.SelectImageViewHolder>() {
    class SelectImageViewHolder(val binding:ItemViewImageSelectionBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectImageViewHolder {
        return SelectImageViewHolder(ItemViewImageSelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    override fun onBindViewHolder(holder: SelectImageViewHolder, position: Int) {
        val image = imageUris [position]
        holder.binding.apply {
            ivImage.setImageURI(image)
        }
        holder.binding.closeButton.setOnClickListener {
            if(position<imageUris.size){
                imageUris.removeAt(position)
                notifyItemRemoved(position)

            }
        }
    }
}