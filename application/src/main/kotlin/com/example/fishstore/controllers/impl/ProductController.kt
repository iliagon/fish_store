package com.example.fishstore.controllers.impl

import com.example.fishstore.controllers.api.OrdersApi
import com.example.fishstore.controllers.api.ProductsApi
import com.example.fishstore.models.dto.OrderBaseDto
import com.example.fishstore.models.dto.OrderExtendedResponseDto
import com.example.fishstore.models.dto.OrderWithIdDto
import com.example.fishstore.models.dto.ProductBaseDto
import com.example.fishstore.models.dto.ProductWithIdDto
import com.example.fishstore.sevice.OrderService
import com.example.fishstore.sevice.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class ProductController(
    val prodService: ProductService
) : ProductsApi {

    override fun deleteProduct(productId: Long): ResponseEntity<Unit> {
        prodService.delete(productId)
        return ResponseEntity.noContent().build();
    }

    override fun getAllProducts(): ResponseEntity<List<ProductWithIdDto>> {
        val products = prodService.findAll()
        return ResponseEntity.ok(products)
    }

    override fun getProductById(productId: Long): ResponseEntity<ProductWithIdDto> {
        val product = prodService.find(productId)
        return ResponseEntity.ok(product)
    }

    override fun postProduct(productBaseDto: ProductBaseDto?): ResponseEntity<ProductWithIdDto> {
        val product = prodService.create(productBaseDto!!)
        return ResponseEntity.ok(product)
    }

    override fun putProduct(productId: Long, productBaseDto: ProductBaseDto?): ResponseEntity<ProductWithIdDto> {
        val products = prodService.update(productId, productBaseDto!!)
        return ResponseEntity.ok(products)
    }
}