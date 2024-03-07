package edu.mirea.onebeattrue.productlist.domain.repository

import edu.mirea.onebeattrue.productlist.domain.entity.Product

interface SearchRepository {
    suspend fun search(query: String): List<Product>
}