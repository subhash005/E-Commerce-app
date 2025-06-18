package com.example.e_commerce.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.e_commerce.Cartlistener
import com.example.e_commerce.R
import com.example.e_commerce.adapters.AdapterCartProducts
import com.example.e_commerce.databinding.ActivityMainBinding
import com.example.e_commerce.databinding.ActivityUsersMainBinding
import com.example.e_commerce.databinding.BsCartProductsBinding
import com.example.e_commerce.databinding.ItemViewProductBinding
import com.example.e_commerce.roomdb.CartProductTable
import com.example.e_commerce.viewmodels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class UsersMainActivity : AppCompatActivity() , Cartlistener {
    private lateinit var binding: ActivityUsersMainBinding
    private val viewModel : UserViewModel by viewModels()
    private lateinit var cartProductList:List<CartProductTable>
    private lateinit var adapterCantProducts: AdapterCartProducts


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityUsersMainBinding.inflate(layoutInflater)

        getTotalItemCountInCart()

        onCartClicked()
        getALlCartProducts()
        onNextButtonClicked()

        setContentView(binding.root)
    }

    private fun onNextButtonClicked() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(  this, OrderPlaceActivity::class.java))
        }
    }

    private fun getALlCartProducts(){
        viewModel.getAll().observe(this){

                cartProductList=it

        }
    }

    private fun onCartClicked() {
        binding.llItemCart.setOnClickListener {
            val bsCartProductsBinding=BsCartProductsBinding.inflate(LayoutInflater.from(this))
            val bs = BottomSheetDialog ( this)
            bs.setContentView(bsCartProductsBinding.root)


            bsCartProductsBinding.tvNumberOfProductCount.text=binding.tvNumberOfProductCount.text
            bsCartProductsBinding.btnNext.setOnClickListener {
                startActivity(Intent(  this, OrderPlaceActivity::class.java))
            }
            adapterCantProducts= AdapterCartProducts()
            bsCartProductsBinding.rvProductsItems.adapter=adapterCantProducts
            adapterCantProducts.differ.submitList(cartProductList)

            bs.show()

        }
    }

    private fun getTotalItemCountInCart() {
        viewModel.fetchTotalCartItemCount().observe(this){
            if(it > 0){
                binding.llCart.visibility=View.VISIBLE
                binding.tvNumberOfProductCount.text=it.toString()

            }
            else{
                binding.llCart.visibility=View.GONE

            }
        }
    }

    override fun showCartLayout(itemCount : Int) {

        val previousCount = binding.tvNumberOfProductCount.text.toString().toInt()
        val updatedCount = previousCount + itemCount

        if (updatedCount>0){
            binding.llCart.visibility=View.VISIBLE
            binding.tvNumberOfProductCount.text=updatedCount.toString()
        }else{
            binding.llCart.visibility=View.GONE
            binding.tvNumberOfProductCount.text="0"
        }
    }

    override fun savingCartItemCount(itemCount: Int) {
        viewModel.fetchTotalCartItemCount().observe(this){
            viewModel.savingCartItemCount(it + itemCount)

        }
//        val previousCount = binding.tvNumberOfProductCount.text.toString().toInt()
    }

    override fun hideCartLayout() {
        binding.llCart.visibility=View.GONE
        binding.tvNumberOfProductCount.text="0"
    }
}