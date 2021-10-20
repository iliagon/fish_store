package com.example.fishstore.repository

import com.example.fishstore.models.entity.OrderEntity
import com.example.fishstore.models.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun findByIdIn(id: Collection<Long>): Set<ProductEntity>
}