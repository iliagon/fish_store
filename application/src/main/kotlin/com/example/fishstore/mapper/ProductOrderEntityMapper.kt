package com.example.fishstore.mapper

import com.example.fishstore.models.dto.OrderBaseDtoProducts
import com.example.fishstore.models.dto.OrderExtendedResponseDtoProducts
import com.example.fishstore.models.entity.OrderProductEntity
import org.springframework.stereotype.Component
import java.net.URI

@Component
class ProductOrderEntityMapper(
    private val productMapper: ProductMapper
) {
    fun toOrderBaseDtoProducts(orderProductEntities: List<OrderProductEntity>): List<OrderBaseDtoProducts> {
        return orderProductEntities.map {
            OrderBaseDtoProducts(
                id = it.pk.product!!.id!!,
                quantity = it.quantity
            )
        }
    }

    fun toOrderExtendedResponseDtoProducts(orderProductEntities: List<OrderProductEntity>): List<OrderExtendedResponseDtoProducts> {
        return orderProductEntities.map {
            OrderExtendedResponseDtoProducts(
                product = productMapper.toProductWithIdDto(it.pk.product!!),
                quantity = it.quantity
            )
        }
    }
}