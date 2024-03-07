package edu.mirea.onebeattrue.productlist.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductListDto(
    @SerializedName("products") val products: List<ProductDto>
)
