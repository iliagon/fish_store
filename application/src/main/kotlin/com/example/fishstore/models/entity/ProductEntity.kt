package com.example.fishstore.models.entity

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "PRODUCT")
class ProductEntity(
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "description", nullable = true)
    val description: String,
    @Column(name = "price", nullable = false)
    val price: BigDecimal,
    @Column(name = "imageUri", nullable = false)
    val imageUri: String,
) : BaseEntity<Long>()