package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.databinding.ItemViewBestsellerBinding
import com.example.e_commerce.models.Bestseller

class AdapterBestseller(val onSeAllButtonClicked: (Bestseller) -> Unit) : RecyclerView.Adapter<AdapterBestseller.BestsellerViewHolder>() {

    inner class BestsellerViewHolder(val binding: ItemViewBestsellerBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Bestseller>() {
        override fun areItemsTheSame(oldItem: Bestseller, newItem: Bestseller): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Bestseller, newItem: Bestseller): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestsellerViewHolder {
        val binding = ItemViewBestsellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestsellerViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BestsellerViewHolder, position: Int) {
        val productType = differ.currentList[position]
        holder.binding.apply {
            tvCategory.text = productType.productType
            tvTotalProducts.text = "${productType.products?.size ?: 0} products"

            val productImages = productType.products?.mapNotNull { it.productImageUris?.firstOrNull() } ?: emptyList()
            val imageViews = listOf(ivProduct1, ivProduct2, ivProduct3)

            // Hide all image views initially
            imageViews.forEach { it.visibility = View.GONE }

            // Display available product images
            productImages.take(imageViews.size).forEachIndexed { index, imageUrl ->
                imageViews[index].visibility = View.VISIBLE
                Glide.with(holder.itemView.context)
                    .load(imageUrl)
                    .into(imageViews[index])
            }

            // Display remaining product count if there are more than 3 products
            val remainingProducts = (productType.products?.size ?: 0) - imageViews.size
            if (remainingProducts > 0) {
                tvProductCount.visibility = View.VISIBLE
                tvProductCount.text = "+$remainingProducts"
            } else {
                tvProductCount.visibility = View.GONE
            }
        }
        holder.itemView.setOnClickListener {
            onSeAllButtonClicked(productType)
        }
    }

    fun submitList(list: List<Bestseller>) {
        differ.submitList(list)
    }
}
