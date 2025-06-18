package com.example.e_commerce.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.Cartlistener
import com.example.e_commerce.R
import com.example.e_commerce.Utils
import com.example.e_commerce.adapters.AdapterProduct
import com.example.e_commerce.databinding.FragmentSearchBinding
import com.example.e_commerce.databinding.ItemViewProductBinding
import com.example.e_commerce.models.Product
import com.example.e_commerce.roomdb.CartProductTable
import com.example.e_commerce.viewmodels.UserViewModel
import kotlinx.coroutines.launch


class searchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapterProduct: AdapterProduct
    val viewModel : UserViewModel by viewModels()
    private var cartListener : Cartlistener?=null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(layoutInflater)

        getAlltheProducts()
        backToHomeFragment()
        searchProducts()
        setStatusBarColor()



        return binding.root
    }

    private fun searchProducts() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
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

    private fun backToHomeFragment() {
        binding.arrowback.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)

        }
    }

    private fun getAlltheProducts() {
        binding.shimmerViewContainer.visibility=View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllTheProducts().collect{


                if(it.isEmpty()){
                    binding.rvProducts.visibility = View.GONE
                    binding. tvText.visibility = View. VISIBLE
                }else{
                    binding.rvProducts.visibility = View.VISIBLE
                    binding. tvText.visibility = View. GONE
                }

                adapterProduct=AdapterProduct(
                    ::onAddButtonCLicked,
                    ::onIncrementButtonCLicked,
                    ::onDecrementButtonClicked
                )
                binding.rvProducts.adapter=adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList = it as ArrayList<Product>
                binding.shimmerViewContainer.visibility=View.GONE
            }
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

    private fun onAddButtonCLicked(product: Product , productBinding:ItemViewProductBinding) {
        productBinding.tvAdd.visibility=View.GONE
        productBinding.llProductCount.visibility=View.VISIBLE

        // step 1
        var itemCount = productBinding.tvProductCount.text.toString().toInt()
        itemCount++
        productBinding.tvProductCount.text=itemCount.toString()

        cartListener?.showCartLayout(1)

        //step 2
        product.itemCount=itemCount
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(1)
            saveProductInRoomDb(product)
            viewModel.updateItemCount(product, itemCount)
        }


    }

    private fun onIncrementButtonCLicked(product: Product, productBinding: ItemViewProductBinding){
        var itemCountInc = productBinding.tvProductCount.text.toString().toInt()
        itemCountInc++
        if (product.productStock!! + 1>itemCountInc){
            productBinding.tvProductCount.text=itemCountInc.toString()

            cartListener?.showCartLayout(1)

            //step 2
            product.itemCount=itemCountInc
            lifecycleScope.launch {
                cartListener?.savingCartItemCount(1)
                saveProductInRoomDb(product)
                viewModel.updateItemCount(product, itemCountInc)

            }
        }
        else{
            Utils.showToast(requireContext(),"Can't add more item of this")
        }


    }

    private fun onDecrementButtonClicked(product: Product, productBinding: ItemViewProductBinding){

        var itemCountDec = productBinding.tvProductCount.text.toString().toInt()
        itemCountDec--


        //step 2
        product.itemCount=itemCountDec
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(-1)
            saveProductInRoomDb(product)
            viewModel.updateItemCount(product, itemCountDec)

        }

        if(itemCountDec > 0){
            productBinding.tvProductCount.text=itemCountDec.toString()
        }
        else{
            lifecycleScope.launch { viewModel.deleteCartProduct((product.productRandomId!!)) }
            productBinding.tvAdd.visibility=View.VISIBLE
            productBinding.llProductCount.visibility=View.GONE
            productBinding.tvProductCount.text="0"
        }


        cartListener?.showCartLayout(-1)

    }

    private fun saveProductInRoomDb(product: Product) {
        val cartProductTable = CartProductTable(
            productId = product.productRandomId!! ,
            productTitle = product.productTitle,
            productQuantity = product.productQuantity.toString()+product.productUnit.toString(),
            productPrice = "₹"+"${product.productPrice}",
            productCount = product.itemCount ,
            productStock = product.productStock ,
            productImage = product.productImageUris?.get(0)!!,
            productCategory = product.productCategory,
            adminUid = product.adminUid,
            productType=product.productType

        )
        lifecycleScope.launch {
            viewModel.insertCartProduct(cartProductTable)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Cartlistener){
            cartListener = context

        }else{
            throw ClassCastException("please implement cart listener")
        }
    }

}