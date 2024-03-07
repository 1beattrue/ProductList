package edu.mirea.onebeattrue.productlist.data.repository

import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.domain.repository.ProductListRepository
import kotlinx.coroutines.flow.Flow

class ProductListRepositoryImpl : ProductListRepository {
    override val productList: Flow<List<Product>>
        get() = TODO("Not yet implemented")

    override fun loadNextData() {
        TODO("Not yet implemented")
    }
}