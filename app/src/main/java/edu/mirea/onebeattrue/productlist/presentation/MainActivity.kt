package edu.mirea.onebeattrue.productlist.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import edu.mirea.onebeattrue.productlist.data.network.api.ApiFactory
import edu.mirea.onebeattrue.productlist.presentation.ui.theme.ProductListTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("MainActivity", ApiFactory.apiService.loadProducts().products.toString())
            Log.d("MainActivity", ApiFactory.apiService.searchProducts("iPhone").products.toString())
        }


        setContent {
            ProductListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}