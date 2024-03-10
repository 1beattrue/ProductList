package edu.mirea.onebeattrue.productlist.domain.entity

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@[Parcelize Immutable]
data class Product(
    val id: Int,

    val title: String,
    val price: Int,
    val thumbnail: String,
    val brand: String,

    val images: List<String>,
    val rating: Float,
    val description: String
) : Parcelable
