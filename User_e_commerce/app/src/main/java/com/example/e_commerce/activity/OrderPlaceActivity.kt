package com.example.e_commerce.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.Cartlistener
import com.example.e_commerce.R
import com.example.e_commerce.Utils
import com.example.e_commerce.adapters.AdapterCartProducts
import com.example.e_commerce.databinding.ActivityOrderPlaceBinding
import com.example.e_commerce.databinding.AddressLayoutBinding
import com.example.e_commerce.models.Orders
import com.example.e_commerce.viewmodels.UserViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import kotlinx.coroutines.launch
import org.json.JSONObject

class OrderPlaceActivity : AppCompatActivity() , PaymentResultWithDataListener {
    private val viewModel : UserViewModel by viewModels()
    private lateinit var adapterCantProducts: AdapterCartProducts
    private var cartListener : Cartlistener?=null




    private lateinit var binding : ActivityOrderPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityOrderPlaceBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setStatusBarColor()
        backToUserMainActvity()
        getAllCartProducts()
        onPlaceOrderClicked()

        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_test_QkibI9jBzMIk5v" )

    }





    private fun onPlaceOrderClicked() {
        binding.btnNext.setOnClickListener {
            viewModel.getAddressStatus().observe(this){status->
                if (status){
                    //payment work
                    val grandTotal = binding.tvGrandTotal.text.toString().toInt()
                    val amountInPaise = grandTotal * 100
                    initializePhonePay(amountInPaise)

                }
                else{
                    val addressLayoutBinding=AddressLayoutBinding.inflate(LayoutInflater.from(this))


                    val alertDialog=AlertDialog.Builder(this)
                        .setView(addressLayoutBinding.root)
                        .create()
                    alertDialog.show()

                    addressLayoutBinding.btnAdd.setOnClickListener{
                        saveAddress(alertDialog,addressLayoutBinding)
                    }

                }
            }
        }
    }

    private fun initializePhonePay(amount: Int) {
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Order Charges")
            //You can omit the image option to fetch the image from the Dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",amount)//pass amount in currency subunits

            val retryObj =JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","Subhash@example.com")
            prefill.put("contact","8800639017")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun saveAddress(alertDialog: AlertDialog, addressLayoutBinding: AddressLayoutBinding) {
        Utils.showDialog(this,"Processing...")
        val userPinCode = addressLayoutBinding.etPinCode.text.toString()
        val userPhoneNumber = addressLayoutBinding.etPhoneNumber.text.toString()
        val userState = addressLayoutBinding.etState.text.toString()
        val userDistrict = addressLayoutBinding.etDistrict.text.toString()
        val userAddress = addressLayoutBinding.etDescriptiveAddress.text.toString()

        val address="$userPinCode,$userDistrict($userState),$userAddress,$userPhoneNumber"

//        val users= Users(
//            userAddress=address
//        )

        lifecycleScope.launch {
            viewModel.saveUserAddress(address)
            viewModel.saveAddressStatus()
        }
        Utils.showToast(this,"Saved....")
        alertDialog.dismiss()
//        Utils.hideDialog()
        val grandTotal = binding.tvGrandTotal.text.toString().toInt()
        val amountInPaise = grandTotal * 100
        initializePhonePay(amountInPaise)
    }

    private fun backToUserMainActvity() {
        binding.tbOrderFragment.setNavigationOnClickListener {
            startActivity(Intent(this,UsersMainActivity::class.java))
            finish()
        }
    }

    private fun getAllCartProducts(){
        viewModel.getAll().observe(this){cartProductList->

            adapterCantProducts = AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCantProducts
            adapterCantProducts.differ.submitList(cartProductList)

            var totalPrice = 0
            for(products in cartProductList){
                val price = products.productPrice?.substring(1)?.toInt()
                val itemCount = products.productCount!!
                totalPrice+=(price?.times(itemCount)!!)
        }
            binding.tvSubTotal.text=totalPrice.toString()
            if(totalPrice<200){
                binding.tvDeliveryCharge.text="₹15"
                totalPrice+=15
            }
            binding.tvGrandTotal.text=totalPrice.toString()
         }
    }

    private fun setStatusBarColor() {
        window?.apply {
            // Set the status bar color
            statusBarColor = ContextCompat.getColor(this@OrderPlaceActivity, R.color.blue)

            // If the Android version is Marshmallow (API 23) or higher, set the light status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

//    override fun onPaymentSuccess(p0: String?) {
//        Utils.showToast(this,"Payment Succuss")
//        startActivity(Intent(this,UsersMainActivity::class.java))
//    }
//
//    override fun onPaymentError(p0: Int, p1: String?) {
//        Utils.showToast(this,"Error : ${p1}")
//        Utils.showToast(this,"Payment Failedddd")
//
//    }

//    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
//        Utils.showToast(this,"Payment Succuss")
//
//        saveOrder()
//        viewModel.deleteCartProducts()
//        viewModel.savingCartItemCount(0)
//        cartListener?.hideCartLayout()
//        // order save, delete products
//        Utils.hideDialog()
//
//        startActivity(Intent(this,UsersMainActivity::class.java))
//
//    }
override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
    Utils.showToast(this, "Payment Success")

    lifecycleScope.launch {
        saveOrder()
        viewModel.deleteCartProducts()
        viewModel.savingCartItemCount(0)
        cartListener?.hideCartLayout()
        Utils.hideDialog()
        startActivity(Intent(this@OrderPlaceActivity, UsersMainActivity::class.java))
    }
}




    private fun saveOrder() {
        viewModel.getAll().observe(this){cartProductsList->
            if(cartProductsList.isNotEmpty()){
                viewModel.getUserAddress {address->
                    val order = Orders(
                        orderId=Utils.getRandomId(),orderList = cartProductsList,
                        userAddress = address,orderStatus = 0, orderDate = Utils.getCurrentDate(),
                        orderingUserId = Utils.getCurrentUserId()
                    )
                    viewModel.saveOrderedProducts(order)
                }
                for(products in cartProductsList){
                    val count = products.productCount
                    val stock = products.productStock?. minus(count!!)
                    if (stock != null) {
                        viewModel.saveProductsAfterOrder(stock,products)
                    }
                }
            }


        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Utils.showToast(this,"Error : ${p1}")
        Utils.showToast(this,"Payment Failedddd")
    }
}