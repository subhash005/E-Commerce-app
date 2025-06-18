package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.e_commerce.FilteringProducts
import com.example.e_commerce.databinding.ItemViewProductBinding
import com.example.e_commerce.models.Product

class AdapterProduct(
    val onAddButtonCLicked: (Product, ItemViewProductBinding) -> Unit,
    val onIncrementButtonCLicked: (Product, ItemViewProductBinding) -> Unit,
    val onDecrementButtonClicked: (Product, ItemViewProductBinding) -> Unit
) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(), Filterable {

    inner class ProductViewHolder(val binding: ItemViewProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemViewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            // Handle image slider
            val imageList = ArrayList<SlideModel>()
            product.productImageUris?.forEach { uri ->
                imageList.add(SlideModel(uri.toString()))
            }
            ivImageSlider.setImageList(imageList)

            // Set product title, quantity, and price
            tvProductTitle.text = product.productTitle
            val quantity = "${product.productQuantity}${product.productUnit}"
            tvProductQuantity.text = quantity
            tvProductPrice.text = "₹ ${product.productPrice}"

            // Handle item count visibility
            if ((product.itemCount ?: 0) > 0) {
                tvProductCount.text = product.itemCount.toString()
                tvAdd.visibility = View.GONE
                llProductCount.visibility = View.VISIBLE
            } else {
                tvAdd.visibility = View.VISIBLE
                llProductCount.visibility = View.GONE
            }

            // Click listeners
            tvAdd.setOnClickListener {
                onAddButtonCLicked(product, this)
            }
            tvIncrementCount.setOnClickListener {
                onIncrementButtonCLicked(product, this)
            }
            tvDecrementCount.setOnClickListener {
                onDecrementButtonClicked(product, this)
            }
        }
    }

    private var filter: FilteringProducts? = null
    var originalList = ArrayList<Product>()

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilteringProducts(this, originalList)
        }
        return filter as FilteringProducts
    }
}

//package com.example.e_commerce.adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Filter
//import android.widget.Filterable
//import androidx.recyclerview.widget.AsyncListDiffer
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.denzcoskun.imageslider.models.SlideModel
//import com.example.e_commerce.FilteringProducts
//import com.example.e_commerce.databinding.ItemViewProductBinding
//import com.example.e_commerce.models.Product
//
//class AdapterProduct(
//    val onAddButtonCLicked: (Product, ItemViewProductBinding) -> Unit,
//    val onIncrementButtonCLicked: (Product, ItemViewProductBinding) -> Unit,
//    val onDecrementButtonClicked: (Product, ItemViewProductBinding) -> Unit
//) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(),Filterable{
//
//    class ProductViewHolder(val binding: ItemViewProductBinding) : RecyclerView.ViewHolder(binding.root) {
//
//
//    }
//
//    val diffutil =object : DiffUtil.ItemCallback<Product>(){
//        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//            return oldItem.productRandomId==newItem.productRandomId
//        }
//
//        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//            return oldItem==newItem
//        }
//
//    }
//
//    val differ=AsyncListDiffer(this,diffutil)
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
//
//    }
//
//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = differ.currentList[position]
//
//        holder.binding.apply {
//            val imageList = ArrayList<SlideModel> ()
//
//            val productImage = product.productImageUris
//
//            for(i in 0 until productImage?.size!!){
//                imageList.add(SlideModel(product.productImageUris!![i].toString()))
//            }
//
//            ivImageSlider.setImageList(imageList)
//            tvProductTitle.text=product.productTitle
//            val quantity = product.productQuantity.toString() + product.productUnit
//            tvProductQuantity.text=quantity
//            tvProductPrice.text="₹ "+product.productPrice
//
//            if (product.itemCount!! >0){
//                tvProductCount.text = product.itemCount. toString()
//                tvAdd.visibility= View.GONE
//                llProductCount.visibility= View.VISIBLE
//            }
//
//            tvAdd.setOnClickListener {
//                onAddButtonCLicked(product,this)
//            }
//
//            tvIncrementCount.setOnClickListener {
//                onIncrementButtonCLicked(product,this)
//            }
//            tvDecrementCount.setOnClickListener {
//                onDecrementButtonClicked(product,this)
//            }
//        }
//    }
//
//
//    private var filter: FilteringProducts? = null
//    var originalList = ArrayList<Product>()
//
//    override fun getFilter(): Filter {
//        if(filter == null)return FilteringProducts ( this,originalList)
//        return filter as FilteringProducts
//    }
//
//
//
//
//
//}
