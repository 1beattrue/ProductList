package edu.mirea.onebeattrue.productlist.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import edu.mirea.onebeattrue.productlist.applicationComponent
import edu.mirea.onebeattrue.productlist.presentation.ui.theme.ProductListTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    val repository = applicationComponent.getRepository()
                    coroutineScope.launch {
                        repository.products.collect {
                            Log.d("MainActivity", it.size.toString())
                        }
                    }


                    Button(onClick = { coroutineScope.launch {
                        repository.loadNextProducts()
                    } }) {

                    }
                }
            }
        }
    }
}