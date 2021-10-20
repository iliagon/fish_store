package com.example.fishstore.models.entity

import com.example.fishstore.models.business.OrderStatus
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "ORDER")
class OrderEntity(
    @Column(name = "order_status", nullable = false)
    val orderStatus: OrderStatus,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(name = "ORDER_PRODUCT",
        joinColumns = [JoinColumn(name = "order_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")])
    val products: Set<ProductEntity>
) : BaseEntity<Long>() {

    @CreatedDate
    @Column(name = "created", updatable = false, nullable = false)
    lateinit var created: LocalDateTime
}