package com.example.e_commerce.models

data class OrderedItems(
    val orderId: String? = null,
    val itemDate: String? = null,
    val itemStatus: Int? = null,
    val itemTitle: String? = null,
    val itemPrice: Int? = null
)
