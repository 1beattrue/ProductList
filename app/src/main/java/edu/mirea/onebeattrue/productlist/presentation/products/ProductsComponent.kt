package edu.mirea.onebeattrue.productlist.presentation.products

import edu.mirea.onebeattrue.productlist.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface ProductsComponent {

    val model: StateFlow<ProductsStore.State>

    fun onLoadNextData()

    fun onClickSearch()

    fun onClickProduct(product: Product)
}