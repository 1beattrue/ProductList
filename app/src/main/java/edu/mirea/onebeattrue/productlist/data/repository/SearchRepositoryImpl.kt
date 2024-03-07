package edu.mirea.onebeattrue.productlist.data.repository

import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.domain.repository.SearchRepository

class SearchRepositoryImpl : SearchRepository {
    override suspend fun search(query: String): List<Product> {
        TODO("Not yet implemented")
    }
}