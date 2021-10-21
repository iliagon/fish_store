package com.example.fishstore.sevice

import com.example.fishstore.config.NotFoundException
import com.example.fishstore.mapper.OrderMapper
import com.example.fishstore.models.business.OrderStatus
import com.example.fishstore.models.dto.OrderBaseDto
import com.example.fishstore.models.dto.OrderExtendedResponseDto
import com.example.fishstore.models.dto.OrderResponseDto
import com.example.fishstore.models.entity.OrderEntity
import com.example.fishstore.models.entity.OrderProductEntity
import com.example.fishstore.models.entity.OrderProductPk
import com.example.fishstore.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val mapper: OrderMapper,
    val productService: ProductService
) {
    fun create(orderDto: OrderBaseDto): OrderResponseDto {
        val orderProductEntities = constructOrderProductEntities(orderDto)
        val savedOrder = orderRepository.save(OrderEntity(OrderStatus.PAID, orderProductEntities))
        return mapper.toOrderResponseDto(savedOrder)
    }

    fun update(id: Long, orderDto: OrderBaseDto): OrderResponseDto {
        orderRepository.findById(id).orElseThrow { notFoundException(id) }
        val orderProductEntities = constructOrderProductEntities(orderDto)
        val savedOrder = orderRepository.save(OrderEntity(OrderStatus.PAID, orderProductEntities).apply { this.id = id })
        return mapper.toOrderResponseDto(savedOrder)
    }

    private fun constructOrderProductEntities(orderDto: OrderBaseDto): List<OrderProductEntity> {
        val productEntities = productService.findEntitiesByIds(orderDto.products.map { it.id }.toSet())
        val orderProductEntities = productEntities.map { productEntity ->
            val quantity = orderDto.products.first { it.id == productEntity.id!! }.quantity
            OrderProductEntity(OrderProductPk().apply { product = productEntity }, quantity)
        }
        return orderProductEntities
    }

    fun find(id: Long): OrderExtendedResponseDto {
        val savedOrder = orderRepository.findById(id).orElseThrow { notFoundException(id) }
        return mapper.toOrderExtendedResponseDto(savedOrder)
    }

    fun findAll(): List<OrderResponseDto> {
        val savedOrders = orderRepository.findAll()
        return savedOrders.map(mapper::toOrderResponseDto)
    }

    fun delete(id: Long) {
        orderRepository.deleteById(id)
    }

    fun notFoundException(id: Long, ex: Exception? = null) =
        NotFoundException(devMessage = "Product with id = $id was not found", exception = ex)
}