package edu.mirea.onebeattrue.productlist.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.productlist.presentation.details.DetailsComponent
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsComponent
import edu.mirea.onebeattrue.productlist.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Products(val component: ProductsComponent) : Child
        data class Search(val component: SearchComponent) : Child
        data class Details(val component: DetailsComponent) : Child
    }
}