package edu.mirea.onebeattrue.productlist.data.network.api

import edu.mirea.onebeattrue.productlist.data.network.dto.ProductListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun loadProducts(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 20
    ): ProductListDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): ProductListDto
}