package com.example.admin_e_commerce

import android.util.Log
import android.widget.Filter
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import com.example.admin_e_commerce.adapter.AdapterProduct
import com.example.admin_e_commerce.model.Product
import java.util.Locale

class FilteringProducts(
    val adapter: AdapterProduct,
    val filter:  ArrayList<Product>
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {

        val result = FilterResults()
        if (!constraint.isNullOrEmpty()){
            Log.d("xxxxxxxx f 4", "filter ")

            val filteredList = ArrayList<Product>()

            val query = constraint. toString().trim().uppercase(Locale.getDefault()).split( " ")

            for(products in filter){
                if(query.any{
                        Log.d("xxxxxxxx f 5", "filter")

                        products.productTitle?.uppercase(Locale.getDefault())?.contains(it)==true ||
                                products.productCategory?.uppercase(Locale.getDefault())?.contains(it)==true ||
                                products.productPrice?.toString()?.uppercase(Locale.getDefault())?.contains(it)==true ||
                                products.productType?.uppercase(Locale.getDefault())?.contains(it)==true


                    }){
                    Log.d("xxxxxxxx f 6", "filter ")
                    filteredList.add(products)
                }
            }
            Log.d("xxxxxxxx f 7", "filter ")

            result.values = filteredList
            result.count = filteredList.size


        }else{
            Log.d("xxxxxxxx f 8", "filter ")

            result.values = filter
            result.count = filter.size
        }


        return result
    }

    override fun publishResults(p0: CharSequence?, result: FilterResults?) {
        adapter.differ.submitList(result?. values as ArrayList<Product>)

    }

}