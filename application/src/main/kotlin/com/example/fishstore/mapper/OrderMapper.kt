package com.example.fishstore.mapper

import com.example.fishstore.models.dto.OrderExtendedResponseDto
import com.example.fishstore.models.dto.OrderWithIdDto
import com.example.fishstore.models.entity.OrderEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping

@Mapper(uses = [ProductMapper::class, ProductListMapper::class])
interface OrderMapper {

    @Mapping(source = "products", target = "productIds")
    fun toOrderWithIdDto(entity: OrderEntity): OrderWithIdDto

    fun toOrderExtendedResponseDto(entity: OrderEntity): OrderExtendedResponseDto
}