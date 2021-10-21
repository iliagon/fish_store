package com.example.fishstore.mapper

import com.example.fishstore.models.dto.OrderExtendedResponseDto
import com.example.fishstore.models.dto.OrderResponseDto
import com.example.fishstore.models.entity.OrderEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [ProductMapper::class, ProductListMapper::class, OrderStatusMapper::class, ProductOrderEntityMapper::class])
interface OrderMapper {

    @Mapping(source = "orderItems", target = "products")
    fun toOrderResponseDto(entity: OrderEntity): OrderResponseDto

    @Mapping(source = "orderItems", target = "products")
    fun toOrderExtendedResponseDto(entity: OrderEntity): OrderExtendedResponseDto
}