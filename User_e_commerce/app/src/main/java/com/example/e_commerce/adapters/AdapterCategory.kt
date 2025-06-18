package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ItemViewProductCategoryBinding
import com.example.e_commerce.models.Category

class AdapterCategory(val categoryList: ArrayList<Category>, val onCategoryIconClicked: (Category) -> Unit) :
    RecyclerView.Adapter<AdapterCategory.CategoryViewholder>() {

    class CategoryViewholder (val binding :ItemViewProductCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {
    return CategoryViewholder(ItemViewProductCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {
        val category=categoryList[position]
        holder.binding.apply {
            ivCategoryImage .setImageResource(category.image)
            tvCategoryTitle.text=category.title
        }

        holder.itemView.setOnClickListener {
            onCategoryIconClicked(category)
        }
    }
}