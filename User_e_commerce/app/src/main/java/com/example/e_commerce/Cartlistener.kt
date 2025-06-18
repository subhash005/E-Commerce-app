package com.example.e_commerce

interface Cartlistener {
    fun showCartLayout (itemCount : Int)

    fun savingCartItemCount(itemCount : Int)

    fun hideCartLayout()
}