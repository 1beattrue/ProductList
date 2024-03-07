package edu.mirea.onebeattrue.productlist.domain.usecase

import edu.mirea.onebeattrue.productlist.domain.repository.SearchRepository
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(private val repository: SearchRepository) {
    suspend operator fun invoke(query: String) = repository.search(query)
}