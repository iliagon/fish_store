package com.example.fishstore.repository

import com.example.fishstore.models.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface OrderRepository : JpaRepository<OrderEntity, Long> {
    @Query("select o from OrderEntity o join fetch ProductEntity p where o.id = :id")
    fun findOrderWithProductsById(id: Long): Optional<OrderEntity>
}