package com.example.e_commerce

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.e_commerce.databinding.ProgressDialogBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {

    private var dialog: AlertDialog? = null

    fun showDialog(context: Context, message: String) {
        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        progress.tvMessage.text = message

        dialog = AlertDialog.Builder(context)
            .setView(progress.root)
            .setCancelable(false)
            .create()

        dialog!!.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }


    // for toast
    fun showToast(context : Context , message : String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    private var firebaseAuthInstance: FirebaseAuth? = null

    fun getAuthInstance(): FirebaseAuth {
        if (firebaseAuthInstance == null) {
            firebaseAuthInstance = FirebaseAuth.getInstance()
        }
        return firebaseAuthInstance!! // !! ensures it's not null
    }

//    fun getCurrentUserId(): String{
//        return FirebaseAuth.getInstance().currentUser!!.uid
//    }
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }


    fun getRandomId():String{
        return (1..25).map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }.joinToString("")

    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.now().format(formatter)
    }


}