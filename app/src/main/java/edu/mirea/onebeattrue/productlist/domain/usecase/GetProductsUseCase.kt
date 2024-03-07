package edu.mirea.onebeattrue.productlist.domain.usecase

import edu.mirea.onebeattrue.productlist.domain.repository.ProductListRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductListRepository
) {
    operator fun invoke() = repository.products
}