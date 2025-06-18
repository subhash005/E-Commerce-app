package com.example.e_commerce.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.Utils
import com.example.e_commerce.activity.UsersMainActivity
import com.example.e_commerce.databinding.FragmentOTPBinding
import com.example.e_commerce.models.Users
import com.example.e_commerce.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {
    private val viewModel : AuthViewModel by viewModels()
    private lateinit var binding: FragmentOTPBinding
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOTPBinding.inflate(layoutInflater)

        getUserNUmber ()
        customizingEnteringOTP()
        sendOTP()
        onLoginButtonClicked()
        onBackButtonClicked()



        return binding.root
    }



    private fun onLoginButtonClicked() {
        binding.btnLogin.setOnClickListener {
            Utils.showDialog(requireContext(),"signing you.....")
            val editTexts= arrayOf(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.etOtp5,binding.etOtp6)
            val otp =editTexts.joinToString(""){it.text.toString()}

            if(otp.length < editTexts.size){
                Utils.showToast(requireContext() , "Please enter right otp")
            }else{
                editTexts.forEach { it.text?.clear();it.clearFocus() }
                verify0tp(otp)
            }
        }
    }

    private fun verify0tp(otp: String) {
        val user= Users(uid=Utils.getCurrentUserId(), userPhoneNumber = userNumber, userAddress = " ")
        viewModel.signInWithPhoneAuthCredential(otp, userNumber,user)
        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect{
                if (it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(),"Loggin....")
                    startActivity(Intent(requireActivity(),UsersMainActivity::class.java))

                }
            }
        }

    }





    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP.....")
        viewModel.apply {
            sendOTP(userNumber,requireActivity())
            lifecycleScope.launch {
                otpsent.collect{
                    if (it){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(),"Otp sent to the number....")
                    }

                }
            }

        }
    }

    private fun onBackButtonClicked() {
        binding.tb0tpFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_singInFragment)
        }
    }


    private fun customizingEnteringOTP() {
        val editTexts= arrayOf(binding.etOtp1,binding.etOtp2,binding.etOtp3,binding.etOtp4,binding.etOtp5,binding.etOtp6)
        for (i in editTexts.indices){
            editTexts[i].addTextChangedListener(object  : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(s?.length==1){
                        if (i < editTexts.size-1){
                            editTexts[i+1].requestFocus()
                        }
                    }else if (s?.length==0){
                        if(i>0){
                            editTexts[i-1].requestFocus()
                        }
                    }

                }

            })
        }
    }

    private fun getUserNUmber() {
        val bundle =arguments
        userNumber=bundle?.getString("number").toString()

        binding.tvUserNumber.text=userNumber
    }


}