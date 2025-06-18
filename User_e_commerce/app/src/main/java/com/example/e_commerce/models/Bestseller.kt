package com.example.e_commerce.models

data class Bestseller(
    val id: String ? = null ,
    val productType : String ? = null,
    val products : ArrayList<Product>?=null
)
