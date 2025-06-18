package com.example.e_commerce.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.Utils
import com.example.e_commerce.databinding.FragmentSingInBinding

class singInFragment : Fragment() {
    private lateinit var binding: FragmentSingInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSingInBinding.inflate(layoutInflater)

        setStatusBarColor()
        getUserNumber()
        onContinueButtonClick()


        return binding.root
    }

    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString()
            if(number.isEmpty() || number.length!=10){
                Utils.showToast(requireContext(), "Please enter the correct phone number")

            }else{
                val bundle = Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_singInFragment_to_OTPFragment,bundle)
            }
        }
    }

    private fun getUserNumber(){
        binding.etUserNumber.addTextChangedListener ( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // No implementation needed
            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len =number?.length
                if(len==10){
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),
                        R.color.blue
                    ))
                }else{
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),
                        R.color.grayish
                    ))

                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // No implementation needed
            }

        } )
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