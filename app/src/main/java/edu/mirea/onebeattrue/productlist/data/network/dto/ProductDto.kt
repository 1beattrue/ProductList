package edu.mirea.onebeattrue.productlist.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("brand") val brand: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("images") val images: List<String>,
)
