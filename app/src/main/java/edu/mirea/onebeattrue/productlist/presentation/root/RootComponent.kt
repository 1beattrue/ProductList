package edu.mirea.onebeattrue.productlist.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.productlist.presentation.details.DetailsComponent
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsComponent
import edu.mirea.onebeattrue.productlist.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Products(val component: ProductsComponent) : Child
        class Search(val component: SearchComponent) : Child
        class Details(val component: DetailsComponent) : Child
    }
}