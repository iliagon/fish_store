package com.example.fishstore.sevice

import com.example.fishstore.config.NotFoundException
import com.example.fishstore.mapper.OrderMapper
import com.example.fishstore.models.business.OrderStatus
import com.example.fishstore.models.dto.OrderBaseDto
import com.example.fishstore.models.dto.OrderExtendedResponseDto
import com.example.fishstore.models.dto.OrderWithIdDto
import com.example.fishstore.models.entity.OrderEntity
import com.example.fishstore.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    val repository: OrderRepository,
    val mapper: OrderMapper,
    val productService: ProductService
) {
    fun create(orderDto: OrderBaseDto): OrderWithIdDto {
        val productEntities = productService.findEntitiesByIds(orderDto.productIds.toSet())
        val savedOrder = repository.save(OrderEntity(OrderStatus.PAID, productEntities))
        return mapper.toOrderWithIdDto(savedOrder)
    }

    fun update(id: Long, orderDto: OrderBaseDto): OrderWithIdDto {
        repository.findById(id).orElseThrow { notFoundException(id) }
        val productEntities = productService.findEntitiesByIds(orderDto.productIds.toSet())
        val savedOrder = repository.save(OrderEntity(OrderStatus.PAID, productEntities).apply { this.id = id })
        return mapper.toOrderWithIdDto(savedOrder)
    }

    fun find(id: Long): OrderExtendedResponseDto {
        val savedOrder = repository.findOrderWithProductsById(id).orElseThrow { notFoundException(id) }
        return mapper.toOrderExtendedResponseDto(savedOrder)
    }

    fun findAll(): List<OrderWithIdDto> {
        val savedOrders = repository.findAll()
        return savedOrders.map(mapper::toOrderWithIdDto)
    }

    fun delete(id: Long) {
        repository.deleteById(id)
    }

    fun notFoundException(id: Long, ex: Exception? = null) =
        NotFoundException(devMessage = "Product with id = $id was not found", exception = ex)
}