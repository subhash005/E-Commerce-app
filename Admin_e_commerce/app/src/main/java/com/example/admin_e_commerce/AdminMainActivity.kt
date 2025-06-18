package com.example.admin_e_commerce

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.admin_e_commerce.databinding.ActivityAdminMainBinding
import com.example.admin_e_commerce.databinding.ActivityMainBinding
import com.example.admin_e_commerce.databinding.FragmentSigninBinding

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomMenu,Navigation.findNavController(this,R.id.fragmentContainerView2))

    }
}