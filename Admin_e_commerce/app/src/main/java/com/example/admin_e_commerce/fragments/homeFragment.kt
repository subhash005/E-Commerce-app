package com.example.admin_e_commerce.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.admin_e_commerce.AuthMainActivity
import com.example.admin_e_commerce.Constants
import com.example.admin_e_commerce.R
import com.example.admin_e_commerce.Utils
import com.example.admin_e_commerce.adapter.AdapterProduct
import com.example.admin_e_commerce.adapter.CategoriesAdapter
import com.example.admin_e_commerce.databinding.EditProductLayoutBinding
import com.example.admin_e_commerce.databinding.FragmentHomeBinding
import com.example.admin_e_commerce.model.Categories
import com.example.admin_e_commerce.model.Product
import com.example.admin_e_commerce.viewmodels.AdminViewModel
import com.google.firebase.database.FirebaseDatabase

import kotlinx.coroutines.launch


class homeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterProduct: AdapterProduct
    val viewModel : AdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)

        setStatusBarColor()
        setCategories()
        getAlltheProducts("All")
        searchProducts()
        onLogOut()
        return binding.root
    }

    private fun onLogOut() {
        binding.tbHomeFragment.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menuLogout->{
                    LogOutUser()
                    true
                }

                else -> {false}
            }
        }
    }

    private fun LogOutUser(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()
        builder.setTitle("LogOut")
            .setMessage("Do you want to log out ?")
            .setPositiveButton("yes") { _, _ ->
                viewModel.logOutUser()
                startActivity(Intent(requireContext(), AuthMainActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("No") { _, _ ->
                alertDialog.dismiss()
            }
            .show()
            .setCancelable(false)

    }

    private fun searchProducts() {
        binding.searchEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //not use
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = s. toString().trim()
                adapterProduct.filter?.filter(query)
            }

            override fun afterTextChanged(p0: Editable?) {
                //not use
            }

        })
    }


    private fun getAlltheProducts(category: String) {
        binding.shimmerViewContainer.visibility=View.VISIBLE
        lifecycleScope.launch {
            Log.d("HomeFragment", "Fetching products for category: $category")
            viewModel.fetchAllTheProducts(category).collect{


                if(it.isEmpty()){
                    binding.rvProducts.visibility = View.GONE
                    binding. tvText.visibility = View. VISIBLE
                }else{
                    binding.rvProducts.visibility = View.VISIBLE
                    binding. tvText.visibility = View. GONE
                }

                adapterProduct=AdapterProduct(::onEditButtonclicked)
                binding.rvProducts.adapter=adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList = it as ArrayList<Product>
                binding.shimmerViewContainer.visibility=View.GONE
            }
        }

    }

    private fun setCategories() {
        val categoryList=ArrayList<Categories>()
        for (i in 0 until Constants.allProductsCategoryIcon.size){
            categoryList.add(Categories(Constants.allProductsCategory[i],Constants.allProductsCategoryIcon[i]))

            binding.rvCategories.adapter=CategoriesAdapter(categoryList,::onCategoryClicked)
        }
    }

    fun onCategoryClicked(categories:Categories){
        getAlltheProducts(categories.category)
    }

    private fun onEditButtonclicked(product: Product){
        val editProduct=EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            etProductTitle.setText(product.productTitle)
            etProductQuantity.setText(product.productQuantity.toString())
            etProductUnit.setText(product.productUnit)
            etProductPrice.setText(product.productPrice.toString())
            etProductStock.setText(product.productStock.toString())
            etProductCategory.setText(product.productCategory)
            etProductType.setText(product.productType)
        }
        val alertDialog=AlertDialog.Builder(requireContext())
            .setView(editProduct.root).create()
        alertDialog.show()

        editProduct.btnEdit.setOnClickListener {
            editProduct.apply {
                etProductTitle.isEnabled = true
                etProductQuantity. isEnabled = true
                etProductUnit. isEnabled = true
                etProductPrice.isEnabled = true
                etProductStock. isEnabled = true
                etProductCategory. isEnabled = true
                etProductType. isEnabled = true
            }
        }

        setAutoCompleteTextViews(editProduct)
        editProduct.btnSAVE.setOnClickListener{

            lifecycleScope.launch {
                product.productTitle = editProduct.etProductTitle.text.toString()
                product.productQuantity = editProduct.etProductQuantity.text.toString().toInt()
                product.productUnit = editProduct. etProductUnit.text.toString()
                product.productPrice = editProduct.etProductPrice.text.toString().toInt()
                product.productStock = editProduct.etProductStock.text.toString().toInt()
                product.productCategory = editProduct.etProductCategory.text.toString()
                product.productType = editProduct.etProductType.text.toString()

                viewModel.savingUpdateProducts(product)
            }

            Utils.showToast(requireContext(),"Saved Changes!")
            alertDialog. dismiss()
        }
        editProduct.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes") { _, _ ->
                    val productId = product.productRandomId ?: ""
                    val category = product.productCategory ?: ""
                    val type = product.productType ?: ""

                    val dbRef = FirebaseDatabase.getInstance().reference

                    dbRef.child("Admins").child("AllProducts").child(productId).removeValue()
                    dbRef.child("Admins").child("ProductCategory").child(category).child(productId).removeValue()
                    dbRef.child("Admins").child("ProductType").child(type).child(productId).removeValue()

                    Utils.showToast(requireContext(), "Product Deleted Successfully!")
                    alertDialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


    }

    private fun setAutoCompleteTextViews(editProduct: EditProductLayoutBinding) {

        val units= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allUnits0fProducts)
        val category= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allProductsCategory)
        val productType= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allProductType)

        editProduct.apply {
            etProductType.setAdapter(productType)
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
        }
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            // Set the status bar color
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)

            // If the Android version is Marshmallow (API 23) or higher, set the light status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}



