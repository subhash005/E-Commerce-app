package com.example.e_commerce.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.Utils
import com.example.e_commerce.activity.AuthMainActivity
import com.example.e_commerce.databinding.AddressBookLayoutBinding
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.example.e_commerce.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class profileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        loadUserPhoneNumber()
        setStatusBarColor()
        onBackButtonClicked()
        onOrdersLayoutClicked()
        onAddressBookClicked()
        onLogOutClicked()

        return binding.root
    }

    private fun loadUserPhoneNumber() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("AllUsers/Users/$uid")
            userRef.child("userPhoneNumber").get().addOnSuccessListener { snapshot ->
                val phone = snapshot.value?.toString()
                binding.userPhoneNumber.text = if (!phone.isNullOrEmpty()) "+91 $phone" else "Phone not found"
            }.addOnFailureListener {
                binding.userPhoneNumber.text = "Error loading number"
            }
        } else {
            binding.userPhoneNumber.text = "User not logged in"
        }
    }

    private fun onLogOutClicked() {
        binding.llLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val alertDialog = builder.create()
            builder.setTitle("LogOut")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
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
    }

    private fun onAddressBookClicked() {
        binding.llAddress.setOnClickListener {
            val addressBookLayoutBinding = AddressBookLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            viewModel.getUserAddress { address ->
                addressBookLayoutBinding.etAddress.setText(address.toString())
            }
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(addressBookLayoutBinding.root)
                .create()
            alertDialog.show()
            addressBookLayoutBinding.btnEdit.setOnClickListener {
                addressBookLayoutBinding.etAddress.isEnabled = true
            }
            addressBookLayoutBinding.btnSave.setOnClickListener {
                viewModel.saveAddress(addressBookLayoutBinding.etAddress.text.toString())
                alertDialog.dismiss()
                Utils.showToast(requireContext(), "Address updated.")
            }
        }
    }

    private fun onOrdersLayoutClicked() {
        binding.llOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }
    }

    private fun onBackButtonClicked() {
        binding.tbProfileFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
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
