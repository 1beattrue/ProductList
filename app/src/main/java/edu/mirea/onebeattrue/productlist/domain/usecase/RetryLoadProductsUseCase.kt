package edu.mirea.onebeattrue.productlist.domain.usecase

import edu.mirea.onebeattrue.productlist.domain.repository.ProductListRepository
import javax.inject.Inject

class RetryLoadProductsUseCase @Inject constructor(private val repository: ProductListRepository) {
    suspend operator fun invoke() = repository.retryLoadProducts()
}