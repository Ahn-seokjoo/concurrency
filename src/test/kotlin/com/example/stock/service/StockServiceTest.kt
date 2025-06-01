package com.example.stock.service

import com.example.stock.domain.Stock
import com.example.stock.repository.StockRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.assertEquals

@SpringBootTest
class StockServiceTest @Autowired constructor(
    private val stockService: PessimisticLockStockService,
    private val stockRepository: StockRepository,
) {

    @BeforeEach
    fun before() {
        stockRepository.saveAndFlush(Stock(productId = 1L, quantity = 100L))
    }

    @AfterEach
    fun after() {
        stockRepository.deleteAll()
    }

    @Test
    fun `재고 감소`() {
        val stock = stockRepository.findAnyOneStock()
        stockService.decrease(stock.id, 1L)

        val result = stockRepository.findById(stock.id).orElseThrow()

        assertEquals(99, result.quantity)
    }

    @Test
    fun `동시에 100개 요청`() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        repeat(100) {
            executor.submit {
                try {
                    stockService.decrease(1L, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        val stock = stockRepository.findById(1L).orElseThrow()
        assertEquals(0, stock.quantity)
    }
}
