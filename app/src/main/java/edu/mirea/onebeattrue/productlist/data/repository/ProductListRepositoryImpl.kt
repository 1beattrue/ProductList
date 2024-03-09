package edu.mirea.onebeattrue.productlist.data.repository

import android.util.Log
import edu.mirea.onebeattrue.productlist.data.mapper.toEntities
import edu.mirea.onebeattrue.productlist.data.network.api.ApiService
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.domain.repository.ProductListRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class ProductListRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductListRepository {
    private val loadNextEvent = MutableSharedFlow<Unit>(replay = 1)

    private val loadedProducts = mutableListOf<Product>()
    private var skip = 0

    override val products: Flow<List<Product>>
        get() = flow {
            loadNextEvent.emit(Unit)
            loadNextEvent.collect {
                val nextProducts = apiService.loadProducts(skip, LIMIT).products.toEntities()

                loadedProducts += nextProducts
                skip += LIMIT
                Log.d("ProductListRepositoryImpl", "$skip")
                emit(loadedProducts.toList())
            }
        }
            .retry(2) { throwable ->
                Log.e("ProductListRepositoryImpl", throwable.message.toString())
                delay(RETRY_TIMEOUT_MILLIS)
                true
            }

    override suspend fun loadNextProducts() {
        loadNextEvent.emit(Unit)
    }

    companion object {
        private const val LIMIT = 20
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}