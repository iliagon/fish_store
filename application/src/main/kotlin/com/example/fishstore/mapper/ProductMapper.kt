package com.example.fishstore.mapper

import com.example.fishstore.models.dto.ProductBaseDto
import com.example.fishstore.models.dto.ProductWithIdDto
import com.example.fishstore.models.entity.ProductEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses = [UriToStringMapper::class])
interface ProductMapper {

    fun toProductWithIdDto(entity: ProductEntity): ProductWithIdDto

    @Mapping(target = "id", ignore = true)
    fun toEntity(payload: ProductBaseDto): ProductEntity
}