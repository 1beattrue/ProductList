package edu.mirea.onebeattrue.productlist.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import edu.mirea.onebeattrue.productlist.presentation.details.DetailsContent
import edu.mirea.onebeattrue.productlist.presentation.products.ProductsContent
import edu.mirea.onebeattrue.productlist.presentation.search.SearchContent

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation()
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Details -> DetailsContent(component = instance.component)
            is RootComponent.Child.Products -> ProductsContent(component = instance.component)
            is RootComponent.Child.Search -> SearchContent(component = instance.component)
        }
    }
}