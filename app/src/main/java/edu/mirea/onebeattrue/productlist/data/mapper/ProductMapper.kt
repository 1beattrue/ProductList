package edu.mirea.onebeattrue.productlist.data.mapper

import edu.mirea.onebeattrue.productlist.data.network.dto.ProductDto
import edu.mirea.onebeattrue.productlist.domain.entity.Product

fun ProductDto.toEntity(): Product = Product(
    id = id,
    title = title,
    price = price,
    thumbnail = thumbnail,
    brand = brand,
    images = images,
    rating = rating,
    description = description
)

fun List<ProductDto>.toEntities(): List<Product> = map { it.toEntity() }