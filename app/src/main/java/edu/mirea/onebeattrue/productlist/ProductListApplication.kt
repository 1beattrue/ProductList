package edu.mirea.onebeattrue.productlist

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import edu.mirea.onebeattrue.productlist.di.DaggerApplicationComponent

class ProductListApplication : Application() {
    val component by lazy {
        DaggerApplicationComponent.create()
    }
}

val applicationComponent
    @Composable get() = (LocalContext.current.applicationContext as ProductListApplication).component