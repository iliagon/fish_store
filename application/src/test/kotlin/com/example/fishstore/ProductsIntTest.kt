package com.example.fishstore

import com.example.fishstore.config.ResetDatabaseTestExecutionListener
import com.example.fishstore.mapper.ProductMapper
import com.example.fishstore.models.business.OrderStatus
import com.example.fishstore.models.dto.ProductBaseDto
import com.example.fishstore.models.dto.ProductWithIdDto
import com.example.fishstore.models.entity.OrderEntity
import com.example.fishstore.models.entity.ProductEntity
import com.example.fishstore.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.net.URI
import javax.persistence.EntityManager

@TestExecutionListeners(
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
    listeners = [ResetDatabaseTestExecutionListener::class]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductsIntTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var productRepository: ProductRepository

    private val initialProducts = listOf(
        ProductWithIdDto(
            id = 1,
            name = "name1",
            price = 1.0,
            imageUri = URI("http://testImage1"),
            description = "test description1"
        ),
        ProductWithIdDto(
            id = 2,
            name = "name2",
            price = 2.0,
            imageUri = URI("http://testImag2e"),
            description = "test description2"
        )
    )

    @BeforeEach
    internal fun beforeEach() {
        productRepository.saveAll(initialProducts.map {
            ProductEntity(
                name = it.name,
                price = BigDecimal(it.price),
                imageUri = it.imageUri.toString(),
                description = it.description!!
            )
        })
        val findById = productRepository.findById(1)
        println(findById)
    }

    @Test
    fun `When GET single product then return existing product`() {
        val id = 1;
        val expectedResponse = initialProducts[0]
        webTestClient.get()
            .uri("api/products/$id")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(ProductWithIdDto::class.java)
            .returnResult()
            .responseBody?.apply {
                assertThat(this).isEqualTo(expectedResponse)
            } ?: AssertionError("response body is Null")
    }

    @Test
    fun `When GET all products then return array of products`() {
        webTestClient.get().uri("api/products")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(ProductWithIdDto::class.java)
            .returnResult()
            .responseBody?.apply {
                assertThat(this)
                    .hasSize(2)
                    .containsAll(initialProducts)
            } ?: AssertionError("response body is Null")
    }

    @Test
    fun `When POST product by id then return created product`() {
        val request = ProductBaseDto(
            name = "testName",
            price = 111.0,
            imageUri = URI("http://testImage"),
            description = "test description"
        )
        webTestClient.post().uri("api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(ProductWithIdDto::class.java)
            .returnResult()
            .responseBody?.apply {
                assertThat(id).isNotNull
                assertThat(name).isEqualTo(request.name)
                assertThat(price).isEqualTo(request.price)
                assertThat(imageUri).isEqualTo(request.imageUri)
                assertThat(description).isEqualTo(request.description)
            } ?: AssertionError("response body is Null")
    }

    @Test
    fun `When Put product then return updated product`() {
        val id = 1;
        val request = ProductBaseDto(
            name = "updatedName",
            price = 9999.0,
            imageUri = URI("http://updatedImage"),
            description = "updated description"
        )
        webTestClient.put()
            .uri("api/products/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(ProductWithIdDto::class.java)
            .returnResult()
            .responseBody?.apply {
                assertThat(id).isNotNull
                assertThat(name).isEqualTo(request.name)
                assertThat(price).isEqualTo(request.price)
                assertThat(imageUri).isEqualTo(request.imageUri)
                assertThat(description).isEqualTo(request.description)
            } ?: AssertionError("response body is Null")
    }

    @Test
    fun `When DELETE product then return ok http status without body`() {
        val id = 1;
        webTestClient.delete()
            .uri("api/products/$id")
            .exchange()
            .expectStatus().isNoContent
    }
}
