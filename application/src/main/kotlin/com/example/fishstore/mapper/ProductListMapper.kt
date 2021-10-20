package com.example.fishstore.mapper

import com.example.fishstore.models.entity.ProductEntity
import org.springframework.stereotype.Component
import java.net.URI

@Component
class ProductListMapper {
    fun fromProductEntityToIdList(productEntities: Set<ProductEntity>): List<Long> {
        return productEntities.map { it.id!! }
    }
}