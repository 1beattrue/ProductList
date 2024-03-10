package edu.mirea.onebeattrue.productlist.presentation.extensions

import kotlin.math.roundToInt

fun Int.formattedPrice(): String = "$this $"

fun Float.roundedRating(): Float = (this * 10).roundToInt() / 10f

fun Float.formattedRating(): String = toString()