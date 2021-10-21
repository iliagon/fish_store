package com.example.fishstore.mapper

import com.example.fishstore.models.business.OrderStatus
import org.springframework.stereotype.Component
import java.net.URI
import com.example.fishstore.models.dto.OrderStatusDto

@Component
class OrderStatusMapper {
    fun fromOrderStatusDtoToOrderStatus(orderStatus: OrderStatusDto): OrderStatus {
        return OrderStatus.valueOf(orderStatus.toString().uppercase())
    }

    fun fromOrderStatusToOrderStatusDto(orderStatus: OrderStatus): OrderStatusDto {
        return OrderStatusDto.valueOf(orderStatus.toString().lowercase())
    }
}