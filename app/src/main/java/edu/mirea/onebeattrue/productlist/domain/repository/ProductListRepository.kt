package edu.mirea.onebeattrue.productlist.domain.repository

import edu.mirea.onebeattrue.productlist.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductListRepository {
    val productList: Flow<List<Product>>
    fun loadNextData()
}