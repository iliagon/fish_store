package com.example.fishstore.repository

import com.example.fishstore.models.entity.OrderProductEntity
import com.example.fishstore.models.entity.OrderProductPk
import org.springframework.data.jpa.repository.JpaRepository

interface OrderProductRepository : JpaRepository<OrderProductEntity, OrderProductPk> {
}