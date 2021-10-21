package com.example.fishstore.models.entity

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "ORDER_PRODUCT")
class OrderProductEntity(
    @EmbeddedId
    val pk: OrderProductPk,
    @Column(name = "quantity", nullable = false)
    val quantity: Int
)