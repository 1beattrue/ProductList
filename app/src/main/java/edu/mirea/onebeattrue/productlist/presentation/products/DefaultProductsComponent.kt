package edu.mirea.onebeattrue.productlist.presentation.products

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultProductsComponent @AssistedInject constructor(
    private val storeFactory: ProductsStoreFactory,
    @Assisted("onProductClicked") private val onProductClicked: (Product) -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : ProductsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is ProductsStore.Label.ClickProduct -> onProductClicked(it.product)
                    ProductsStore.Label.ClickSearch -> onSearchClicked()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ProductsStore.State>
        get() = store.stateFlow

    override fun onLoadNextData() {
        store.accept(ProductsStore.Intent.LoadNextData)
    }

    override fun onClickSearch() {
        store.accept(ProductsStore.Intent.ClickSearch)
    }

    override fun onClickProduct(product: Product) {
        store.accept(ProductsStore.Intent.ClickProduct(product))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onProductClicked") onProductClicked: (Product) -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultProductsComponent
    }
}