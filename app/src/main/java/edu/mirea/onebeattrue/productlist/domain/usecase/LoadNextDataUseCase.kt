package edu.mirea.onebeattrue.productlist.domain.usecase

import edu.mirea.onebeattrue.productlist.domain.repository.ProductListRepository
import javax.inject.Inject

class LoadNextDataUseCase @Inject constructor(private val repository: ProductListRepository) {
    suspend operator fun invoke() = repository.loadNextProducts()
}