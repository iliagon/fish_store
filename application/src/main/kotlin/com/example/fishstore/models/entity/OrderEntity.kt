package com.example.fishstore.models.entity

import com.example.fishstore.models.business.OrderStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "SHOP_ORDER")
class OrderEntity(
    @Column(name = "order_status", nullable = false)
    val orderStatus: OrderStatus,
    orderItems: List<OrderProductEntity>
) : BaseEntity<Long>() {

//    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.LAZY)
//    @JoinTable(name = "ORDER_PRODUCT",
//        joinColumns = [JoinColumn(name = "order_id")],
//        inverseJoinColumns = [JoinColumn(name = "product_id")])
//    val products: Set<ProductEntity>,

    @OneToMany(mappedBy = "pk.order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderItems: List<OrderProductEntity>

    init {
        this.orderItems = orderItems.map { it.pk.order = this; it }
    }

    @CreatedDate
    @Column(name = "created", updatable = false, nullable = false)
    lateinit var created: LocalDateTime
}