package com.example.admin_e_commerce.model



data class CartProductTable(

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
