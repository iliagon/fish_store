package com.example.fishstore

import com.example.fishstore.config.ResetDatabaseTestExecutionListener
import com.example.fishstore.models.business.OrderStatus
import com.example.fishstore.models.dto.OrderBaseDto
import com.example.fishstore.models.dto.OrderBaseDtoProducts
import com.example.fishstore.models.dto.OrderExtendedResponseDto
import com.example.fishstore.models.dto.OrderExtendedResponseDtoProducts
import com.example.fishstore.models.dto.OrderResponseDto
import com.example.fishstore.models.dto.OrderStatusDto
import com.example.fishstore.models.dto.ProductWithIdDto
import com.example.fishstore.models.entity.OrderEntity
import com.example.fishstore.models.entity.OrderProductEntity
import com.example.fishstore.models.entity.OrderProductPk
import com.example.fishstore.models.entity.ProductEntity
import com.example.fishstore.repository.OrderRepository
import com.example.fishstore.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
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

@TestExecutionListeners(
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
    listeners = [ResetDatabaseTestExecutionListener::class]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersIntTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository


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
        ),
        ProductWithIdDto(
            id = 3,
            name = "name3",
            price = 3.0,
            imageUri = URI("http://testImage3"),
            description = "test description3"
        )
    )

    private val initialOrders = listOf(
        OrderExtendedResponseDto(
            id = 1,
            orderStatus = OrderStatusDto.paid,
            products = listOf(initialProducts[0], initialProducts[1]).map { OrderExtendedResponseDtoProducts(it, 1) }
        ),
        OrderExtendedResponseDto(
            id = 2,
            orderStatus = OrderStatusDto.paid,
            products = listOf(initialProducts[0], initialProducts[2]).map { OrderExtendedResponseDtoProducts(it, 1) }
        )
    )

    @BeforeEach
    internal fun beforeEach() {
        val savedProducts = productRepository.saveAll(initialProducts.map {
            ProductEntity(
                name = it.name,
                price = BigDecimal(it.price),
                imageUri = it.imageUri.toString(),
                description = it.description!!
            )
        })
        orderRepository.saveAll(initialOrders.map { order ->
            OrderEntity(
                orderStatus = OrderStatus.PAID,
                orderItems = savedProducts
                    .filter { productEntity -> order.products.map { it.product.id }.contains(productEntity.id) }
                    .map {
                        OrderProductEntity(
                            OrderProductPk().apply { product = it }, 1
                        )
                    }
            )
        })
    }

    @Test
    fun `When GET single order then return existing order`() {
        val id = 1;
        val expectedResponse = initialProducts[0]
        webTestClient.get()
            .uri("api/orders/$id")
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
    fun `When GET all orders then return array of orders`() {
        webTestClient.get().uri("api/orders")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(OrderResponseDto::class.java)
            .returnResult()
            .responseBody?.apply {
                assertThat(this)
                    .hasSize(2)
                    .containsAll(initialOrders.map {
                        OrderResponseDto(
                            id = it.id,
                            orderStatus = it.orderStatus,
                            products = it.products.map { OrderBaseDtoProducts(it.product.id, 1) }
                        )
                    })
            } ?: AssertionError("response body is Null")
    }

    @Test
    fun `When POST order by id then return created order`() {
        val productIds = listOf(2L, 3L)
        val request = OrderBaseDto(productIds.map { OrderBaseDtoProducts(it, 1) })
        webTestClient.post().uri("api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(OrderExtendedResponseDto::class.java)
            .returnResult()
            .responseBody?.apply {
                assertThat(id).isNotNull
                assertThat(orderStatus).isEqualTo(OrderStatusDto.paid)
                assertThat(products).isEqualTo(initialProducts.filter { productIds.contains(it.id) })
            } ?: AssertionError("response body is Null")
    }

    @Test
    fun `When Put order then return updated order`() {
        val id = 1;
        val productIds = listOf(2L, 3L)
        val request = OrderBaseDto(productIds.map { OrderBaseDtoProducts(it, 1) })
        webTestClient.put()
            .uri("api/orders/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(OrderExtendedResponseDto::class.java)
            .returnResult()
            .responseBody?.apply {
                assertThat(id).isEqualTo(id)
                assertThat(orderStatus).isEqualTo(OrderStatusDto.paid)
                assertThat(products).isEqualTo(initialProducts.filter { productIds.contains(it.id) })
            } ?: AssertionError("response body is Null")
    }

    @Test
    fun `When DELETE order then return ok http status without body`() {
        val id = 1;
        webTestClient.delete()
            .uri("api/orders/$id")
            .exchange()
            .expectStatus().isOk
    }
}
