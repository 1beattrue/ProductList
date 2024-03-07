package edu.mirea.onebeattrue.productlist.presentation.search

import edu.mirea.onebeattrue.productlist.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun onChangeSearchQuery(query: String)

    fun onClickSearch()

    fun onClickProduct(product: Product)

    fun onClickBack()
}