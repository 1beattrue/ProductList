package edu.mirea.onebeattrue.productlist.data.repository

import edu.mirea.onebeattrue.productlist.data.mapper.toEntities
import edu.mirea.onebeattrue.productlist.data.network.api.ApiService
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<Product> {
        return apiService.searchProducts(query).products.toEntities()
    }
}