package com.example.e_commerce.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="CartProductTable")
data class CartProductTable(

    @PrimaryKey
    val productId: String = "random",
    val productTitle: String? = null,
    val productQuantity: String? = null,
    val productPrice: String? = null,
    var productCount: Int? = null,
    var productStock: Int? = null,
    var productImage: String? = null,
    var productCategory: String? = null,
    var adminUid: String? = null,
    var productType: String? = null

    )
