package edu.mirea.onebeattrue.productlist.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.presentation.details.DefaultDetailsComponent
import edu.mirea.onebeattrue.productlist.presentation.products.DefaultProductsComponent
import edu.mirea.onebeattrue.productlist.presentation.search.DefaultSearchComponent
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val defaultProductsFactory: DefaultProductsComponent.Factory,
    private val defaultDetailsFactory: DefaultDetailsComponent.Factory,
    private val defaultSearchFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = Config.Products,
            handleBackButton = true,
            childFactory = ::child
        )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.Details -> {
            val component = defaultDetailsFactory.create(
                product = config.product,
                onBackClicked = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            RootComponent.Child.Details(component)
        }

        Config.Products -> {
            val component = defaultProductsFactory.create(
                onProductClicked = {
                    navigation.pushNew(Config.Details(it))
                },
                onSearchClicked = {
                    navigation.push(Config.Search)
                },
                componentContext = componentContext
            )
            RootComponent.Child.Products(component)
        }

        Config.Search -> {
            val component = defaultSearchFactory.create(
                onBackClicked = {
                    navigation.pop()
                },
                onProductClicked = {
                    navigation.replaceCurrent(Config.Details(it))
                },
                componentContext = componentContext
            )
            RootComponent.Child.Search(component)
        }
    }

    sealed interface Config : Parcelable {
        @Parcelize
        data object Products : Config

        @Parcelize
        data object Search : Config

        @Parcelize
        data class Details(val product: Product) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}