package edu.mirea.onebeattrue.productlist.presentation.details

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.presentation.details.DetailsStore.Intent
import edu.mirea.onebeattrue.productlist.presentation.details.DetailsStore.Label
import edu.mirea.onebeattrue.productlist.presentation.details.DetailsStore.State
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
    }

    data class State(
        val product: Product
    )

    sealed interface Label {
        data object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {
    fun create(product: Product): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(product),
            executorFactory = ::ExecutorImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> publish(Label.ClickBack)
            }
        }
    }

    companion object {
        private const val STORE_NAME = "DetailsStore"
    }
}
