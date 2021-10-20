package com.example.fishstore.models.entity

import org.springframework.data.annotation.Id
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class OrderProductPk(
    @Column(name = "order_id", nullable = false)
    val orderId: Long,
    @Column(name = "product_id", nullable = false)
    val productId: Long
) : Serializable