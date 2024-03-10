package edu.mirea.onebeattrue.productlist.domain.repository

import edu.mirea.onebeattrue.productlist.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductListRepository {

    val products: Flow<List<Product>>

    suspend fun loadNextProducts()

    suspend fun retryLoadProducts()
}