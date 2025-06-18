package com.example.e_commerce.viewmodels

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.e_commerce.Utils
import com.example.e_commerce.models.Users
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel(){

    private val _verificationId = MutableStateFlow<String?>( null)
    private val _otpSent=MutableStateFlow(false)
    private val _isSignedInSuccessfully=MutableStateFlow(false)
    val isSignedInSuccessfully=_isSignedInSuccessfully
    val otpsent =_otpSent

    private val _isACurrentUser=MutableStateFlow(false)
    var isACurrentUser=_isACurrentUser

    init {
        Utils.getAuthInstance().currentUser?.let {
            _isACurrentUser.value=true
        }
    }

    fun sendOTP(userNumber: String,activity: Activity){
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted:$credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _verificationId.value=verificationId
                _otpSent.value=true
            }
          }
        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+91$userNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, user: Users) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)

        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = Utils.getAuthInstance().currentUser
                    if (firebaseUser != null) {
                        user.uid = firebaseUser.uid  // Ensure UID is assigned
                        FirebaseDatabase.getInstance().getReference("AllUsers")
                            .child("Users")
                            .child(user.uid!!)
                            .setValue(user)
                        _isSignedInSuccessfully.value = true
                    } else {
                        Log.e("AuthError", "FirebaseUser is null after sign-in")
                    }
                } else {
                    Log.e("AuthError", "Sign-in failed: ${task.exception?.message}")
                }
            }
    }

}
