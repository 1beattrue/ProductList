package edu.mirea.onebeattrue.productlist.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.domain.usecase.SearchProductUseCase
import edu.mirea.onebeattrue.productlist.presentation.search.SearchStore.Intent
import edu.mirea.onebeattrue.productlist.presentation.search.SearchStore.Label
import edu.mirea.onebeattrue.productlist.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeSearchQuery(val query: String) : Intent
        data object ClickSearch : Intent
        data object ClickBack : Intent
        data class ClickProduct(val product: Product) : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {
            data object Initial : SearchState
            data object Loading : SearchState
            data object Failure : SearchState
            data object EmptyResult : SearchState
            data class Loaded(val products: List<Product>) : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
        data class ClickProduct(val product: Product) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchProductUseCase: SearchProductUseCase
) {
    fun create(): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeSearchQuery(val query: String) : Msg
        data object Loading : Msg
        data object Failure : Msg
        data class Result(val products: List<Product>) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickSearch -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(Msg.Loading)
                        try {
                            val products = searchProductUseCase(getState().searchQuery)
                            dispatch(Msg.Result(products))
                        } catch (_: Exception) {
                            dispatch(Msg.Failure)
                        }
                    }
                }

                is Intent.ClickProduct -> {
                    publish(Label.ClickProduct(intent.product))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeSearchQuery -> {
                    copy(searchQuery = msg.query)
                }

                Msg.Failure -> {
                    copy(searchState = State.SearchState.Failure)
                }

                Msg.Loading -> {
                    copy(searchState = State.SearchState.Loading)
                }

                is Msg.Result -> {
                    val searchState = if (msg.products.isEmpty()) {
                        State.SearchState.EmptyResult
                    } else {
                        State.SearchState.Loaded(msg.products)
                    }
                    copy(searchState = searchState)
                }
            }
    }

    companion object {
        private const val STORE_NAME = "SearchStore"
    }
}
