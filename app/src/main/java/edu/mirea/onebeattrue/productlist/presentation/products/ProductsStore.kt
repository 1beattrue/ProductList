package edu.mirea.onebeattrue.productlist.presentation.products

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.domain.usecase.GetProductsUseCase
import edu.mirea.onebeattrue.productlist.domain.usecase.LoadNextProductsUseCase
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsStore.Intent
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsStore.Label
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProductsStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data object LoadNextData : Intent
        data object ClickSearch : Intent
        data class ClickProduct(val product: Product) : Intent
    }

    data class State(
        val products: List<Product>,
        val loadingState: LoadingState
    ) {
        sealed interface LoadingState {
            data object Initial : LoadingState
            data object Loading : LoadingState
            data object Failure : LoadingState
            data object WaitForLoad: LoadingState
            data object NothingToLoad : LoadingState
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
    private val loadNextProductsUseCase: LoadNextProductsUseCase
) {
    fun create(): ProductsStore =
        object : ProductsStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                products = listOf(),
                loadingState = State.LoadingState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object ProductListLoading : Action
        data object ProductListLoadingFailure : Action
        data class ProductsLoaded(val products: List<Product>) : Action
    }

    private sealed interface Msg {
        data class ProductsLoaded(val products: List<Product>) : Msg
        data object ProductListLoading : Msg
        data object ProductListLoadingFailure : Msg
        data object NothingToLoad : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.ProductListLoading)
                getProductsUseCase()
                    .catch { dispatch(Action.ProductListLoadingFailure) }
                    .collect {
                        dispatch(Action.ProductsLoaded(it))
                        Log.d("BootstrapperImpl", "collect")
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
                        dispatch(Msg.ProductListLoading)
                        loadNextProductsUseCase()
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ProductsLoaded -> {
                    val products = getState().products
                    if (action.products.size != products.size) {
                        dispatch(Msg.ProductsLoaded(action.products))
                    } else {
                        dispatch(Msg.NothingToLoad)
                    }
                }

                Action.ProductListLoading -> dispatch(Msg.ProductListLoading)
                Action.ProductListLoadingFailure -> dispatch(Msg.ProductListLoadingFailure)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.ProductListLoading -> {
                    copy(loadingState = State.LoadingState.Loading)
                }

                Msg.ProductListLoadingFailure -> {
                    copy(loadingState = State.LoadingState.Failure)
                }

                is Msg.ProductsLoaded -> {
                    copy(
                        products = msg.products,
                        loadingState = State.LoadingState.WaitForLoad
                    )
                }

                Msg.NothingToLoad -> {
                    copy(loadingState = State.LoadingState.NothingToLoad)
                }
            }
    }

    companion object {
        private const val STORE_NAME = "ProductsStore"
    }
}
