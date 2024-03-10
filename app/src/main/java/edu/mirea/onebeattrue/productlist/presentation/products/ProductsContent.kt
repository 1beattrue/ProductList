package edu.mirea.onebeattrue.productlist.presentation.products

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.productlist.R
import edu.mirea.onebeattrue.productlist.domain.entity.Product
import edu.mirea.onebeattrue.productlist.presentation.extensions.formattedPrice
import edu.mirea.onebeattrue.productlist.presentation.extensions.formattedRating
import edu.mirea.onebeattrue.productlist.presentation.extensions.roundedRating
import edu.mirea.onebeattrue.productlist.presentation.ui.theme.RatingStar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsContent(
    modifier: Modifier = Modifier,
    component: ProductsComponent
) {
    val state by component.model.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchCard(
            modifier = Modifier.padding(16.dp),
            onSearchClick = { component.onClickSearch() }
        )
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.products,
                key = { it.id }
            ) { product ->
                ProductCard(
                    modifier = Modifier.animateItemPlacement(),
                    product = product,
                    onProductClick = {
                        component.onClickProduct(it)
                    }
                )
            }
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (state.loadingState) {
                        ProductsStore.State.LoadingState.Initial -> {
                        }

                        ProductsStore.State.LoadingState.Loading -> {
                            Loading()
                        }

                        ProductsStore.State.LoadingState.NothingToLoad -> {
                            NothingToLoad()
                        }

                        ProductsStore.State.LoadingState.WaitForLoad -> {
                            SideEffect {
                                component.onLoadNextData()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchCard(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onSearchClick() }
                .fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
                imageVector = Icons.Rounded.Search,
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun NothingToLoad(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.nothing_to_load),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClick: (Product) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                shape = MaterialTheme.shapes.large
            ),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { onProductClick(product) }
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small),
                model = product.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            ) {
                it.error(R.drawable.empty_image).load(product.thumbnail)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.headlineLarge
                )
                RatingBar(
                    rating = product.rating.roundedRating()
                )
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.End),
                    text = product.price.formattedPrice(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float = 0f
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) {
            Icon(
                imageVector = if (it + 1 <= rating) {
                    Icons.Rounded.Star
                } else if (rating % 1 > 0) {
                    Icons.AutoMirrored.Rounded.StarHalf
                } else {
                    Icons.Rounded.StarBorder
                },
                contentDescription = null,
                tint = RatingStar,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = rating.formattedRating(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}