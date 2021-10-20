package com.example.fishstore.sevice

import com.example.fishstore.config.NotFoundException
import com.example.fishstore.mapper.ProductMapper
import com.example.fishstore.models.dto.ProductBaseDto
import com.example.fishstore.models.dto.ProductWithIdDto
import com.example.fishstore.models.entity.ProductEntity
import com.example.fishstore.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(
    val repository: ProductRepository,
    val mapper: ProductMapper
) {
    fun create(productDto: ProductBaseDto): ProductWithIdDto {
        val product = mapper.toEntity(productDto)
        val savedProduct = repository.save(product)
        return mapper.toProductWithIdDto(savedProduct)
    }

    fun update(id: Long, productDto: ProductBaseDto): ProductWithIdDto {
        repository.findById(id).orElseThrow { notFoundException(id) }
        val entity = mapper.toEntity(productDto).apply { this.id = id }
        val savedProduct = repository.save(entity)
        return mapper.toProductWithIdDto(savedProduct)
    }

    fun find(id: Long): ProductWithIdDto {
        val savedProduct = repository.findById(id).orElseThrow { notFoundException(id) }
        return mapper.toProductWithIdDto(savedProduct)
    }

    fun findEntitiesByIds(ids: Set<Long>): Set<ProductEntity> {
        val products = repository.findByIdIn(ids)
        val notFoundElementIds = ids.minus(products.map { it.id })
        if (notFoundElementIds.isNotEmpty()) {
            throw NotFoundException(
                devMessage = "Products with ids = ${notFoundElementIds.joinToString(",")} was not found"
            )
        }
        return products;
    }

    fun findAll(): List<ProductWithIdDto> {
        val savedEntities = repository.findAll()
        return savedEntities.map(mapper::toProductWithIdDto)
    }

    fun delete(id: Long) {
        repository.deleteById(id)
    }

    fun notFoundException(id: Long, ex: Exception? = null) =
        NotFoundException(devMessage = "Product with id = $id was not found", exception = ex)

}