package com.example.fishstore.models.entity

import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "ORDER_PRODUCT")
class OrderProduct(
    @EmbeddedId
    val orderProductPk: OrderProductPk,
    @Column(name = "quantity", nullable = false)
    val quantity: Int
)