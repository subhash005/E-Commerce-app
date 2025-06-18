package com.example.admin_e_commerce.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.admin_e_commerce.AdminMainActivity
import com.example.admin_e_commerce.Constants
import com.example.admin_e_commerce.R
import com.example.admin_e_commerce.Utils
import com.example.admin_e_commerce.adapter.AdapterSelectedImage
import com.example.admin_e_commerce.databinding.ActivityAdminMainBinding
import com.example.admin_e_commerce.databinding.FragmentAddProductBinding
import com.example.admin_e_commerce.model.Product
import com.example.admin_e_commerce.viewmodels.AdminViewModel
import com.google.firebase.storage.internal.Util
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class addProductFragment : Fragment() {

    private val viewModel:AdminViewModel by viewModels()

    private lateinit var binding: FragmentAddProductBinding
    val selectedImage=registerForActivityResult(ActivityResultContracts.GetMultipleContents()){listOfUri ->
        val fiveimages= listOfUri.take(5)

        imageUris.clear()
        imageUris.addAll(fiveimages)

        binding.rvProductImages.adapter=AdapterSelectedImage(imageUris)

    }
    val imageUris : ArrayList<Uri> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddProductBinding.inflate(layoutInflater)

        setStatusBarColor()
        onImageSelectClicked()
        setAutoCompleteTextViews()
        onAddButtonClicked()

        return binding.root
    }

    private fun onAddButtonClicked() {
        binding.btnAddProduct.setOnClickListener {
            Utils.showDialog(requireContext(),"Uploading images....")
            val productTitle = binding.etProductTitle.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productStock = binding.etProductStock.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()

            if(productTitle.isEmpty()||productQuantity.isEmpty()||productUnit.isEmpty()
                ||productPrice.isEmpty()||productStock.isEmpty()||productCategory.isEmpty()||productType.isEmpty()){
                Utils.hideDialog()
                Utils.showToast(requireContext(),"Empty fields are not allowed")
            }
            else if (imageUris.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(),"Please upload some images")
                }
            }
            else{
                val product = Product(
                    productTitle=productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                    productRandomId=Utils.getRandomId()
                    )
                saveImage(product)
            }
        }
    }

    private fun saveImage(product: Product) {
        viewModel.saveImageInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImagesUploaded.collect{
                if (it){
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(),"Image saved....")

                    }
                    getUrls(product)
                }
            }
        }

    }

    private fun getUrls(product: Product) {
        Utils.showDialog(requireContext(),"Publishing product")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect{
                val urls=it
                product.productImageUris=urls
                saveProduct(product)
            }
        }
    }

    private fun saveProduct(product: Product) {
        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect{
                if(it){
                    Utils.hideDialog()
                    startActivity(Intent(requireActivity() , AdminMainActivity:: class.java))
                    Utils. showToast(requireContext() ,  "Your product is live")
                }
            }
        }
    }


    private fun onImageSelectClicked() {
        binding.btnSelectImage.setOnClickListener {
            selectedImage.launch("image/*")
        }
    }

    private fun setAutoCompleteTextViews(){

        val units=ArrayAdapter(requireContext(),R.layout.show_list,Constants.allUnits0fProducts)
        val category=ArrayAdapter(requireContext(),R.layout.show_list,Constants.allProductsCategory)
        val productType=ArrayAdapter(requireContext(),R.layout.show_list,Constants.allProductType)

        binding.apply {
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