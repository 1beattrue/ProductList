package edu.mirea.onebeattrue.productlist.presentation.extensions

import java.util.Locale

fun Int.formattedPrice(): String = "$this $"

fun Float.formattedRating(): String = String.format(Locale.US,"%.1f", this)