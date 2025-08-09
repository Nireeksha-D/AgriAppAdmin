package com.example.admin.order

data class Order(
    val address: String? = null,
    val deliveryDate: Long? = null,
    val key: String? = null,
    val orderDate: Long? = null,
    val paymentMode: String? = null,
    val price: Int? = null,
    val productName: String? = null,
    val userName: String? = null,
    val userPhone: String? = null
)
