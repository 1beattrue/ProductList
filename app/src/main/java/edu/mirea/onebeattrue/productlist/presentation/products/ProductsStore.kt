package edu.mirea.onebeattrue.productlist.presentation.products

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.domain.usecase.GetProductsUseCase
import edu.mirea.onebeattrue.productlist.domain.usecase.LoadNextDataUseCase
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsStore.Intent
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsStore.Label
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProductsStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data object LoadNextData : Intent
        data object ClickSearch : Intent
        data class ClickProduct(val product: Product) : Intent
    }

    data class State(
        val productList: ProductList,
        val isNextDataLoading: Boolean
    ) {
        sealed interface ProductList {
            data object Initial : ProductList
            data object Loading : ProductList
            data object Failure : ProductList
            data class Loaded(val products: List<Product>) : ProductList
        }
    }

    sealed interface Label {
        data object ClickSearch : Label
        data class ClickProduct(val product: Product) : Label
    }
}

class ProductsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getProductsUseCase: GetProductsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase
) {
    fun create(): ProductsStore =
        object : ProductsStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                productList = State.ProductList.Initial,
                isNextDataLoading = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class ProductsLoaded(val products: List<Product>) : Action
    }

    private sealed interface Msg {
        data class ProductsLoaded(val products: List<Product>) : Msg
        data object ProductListLoading : Msg
        data object NextProductsLoading : Msg
        data object ProductListLoadingFailure : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch(Dispatchers.Default) {
                getProductsUseCase().collect {
                    dispatch(Action.ProductsLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ClickProduct -> {
                    publish(Label.ClickProduct(intent.product))
                }

                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }

                Intent.LoadNextData -> {
                    scope.launch {
                        loadNextDataUseCase()
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ProductsLoaded -> dispatch(Msg.ProductsLoaded(action.products))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.NextProductsLoading -> {
                    copy(isNextDataLoading = true)
                }

                Msg.ProductListLoading -> {
                    copy(productList = State.ProductList.Loading)
                }

                Msg.ProductListLoadingFailure -> {
                    copy(productList = State.ProductList.Failure)
                }

                is Msg.ProductsLoaded -> {
                    copy(
                        productList = State.ProductList.Loaded(msg.products),
                        isNextDataLoading = false
                    )
                }
            }
    }

    companion object {
        private const val STORE_NAME = "ProductsStore"
    }
}
