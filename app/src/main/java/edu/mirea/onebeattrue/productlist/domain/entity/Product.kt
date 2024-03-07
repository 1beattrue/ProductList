package edu.mirea.onebeattrue.productlist.domain.entity

data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val thumbnail: String,
    val brand: String,

    val images: List<String>,
    val rating: Float,
    val description: String
)
