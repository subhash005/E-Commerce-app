package com.example.admin_e_commerce

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_e_commerce.adapter.UserAdapter
import com.example.admin_e_commerce.databinding.FragmentUserDetailsBinding
import com.example.admin_e_commerce.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class userDetailsFragment : Fragment() {

        private lateinit var binding: FragmentUserDetailsBinding
        private val userList = mutableListOf<UserModel>()
        private lateinit var adapter: UserAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            adapter = UserAdapter(userList)
            binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
            binding.rvUsers.adapter = adapter

            setStatusBarColor()
            fetchUsersFromFirebase()
        }

        private fun fetchUsersFromFirebase() {
            val dbRef = FirebaseDatabase.getInstance().getReference("AllUsers/Users")
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (userSnap in snapshot.children) {
                        val user = userSnap.getValue(UserModel::class.java)
                        user?.let { userList.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
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