package com.example.fishstore.controllers.impl

import com.example.fishstore.controllers.api.OrdersApi
import com.example.fishstore.models.dto.OrderBaseDto
import com.example.fishstore.models.dto.OrderExtendedResponseDto
import com.example.fishstore.models.dto.OrderWithIdDto
import com.example.fishstore.models.dto.ProductWithIdDto
import com.example.fishstore.sevice.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class OrderController(
    val orderService: OrderService
) : OrdersApi {
    override fun deleteOrder(orderId: Long): ResponseEntity<Unit> {
        orderService.delete(orderId)
        return ResponseEntity.noContent().build();
    }

    override fun getAllOrders(): ResponseEntity<List<OrderWithIdDto>> {
        val orders = orderService.findAll()
        return ResponseEntity.ok(orders)
    }

    override fun getOrderById(orderId: Long): ResponseEntity<OrderExtendedResponseDto> {
        val order = orderService.find(orderId)
        return ResponseEntity.ok(order)
    }

    override fun postOrder(orderBaseDto: OrderBaseDto?): ResponseEntity<OrderWithIdDto> {
        val orders = orderService.create(orderBaseDto!!)
        return ResponseEntity.ok(orders)
    }

    override fun putOrder(orderId: Long, orderBaseDto: OrderBaseDto?): ResponseEntity<OrderWithIdDto> {
        val orders = orderService.update(orderId, orderBaseDto!!)
        return ResponseEntity.ok(orders)
    }
}