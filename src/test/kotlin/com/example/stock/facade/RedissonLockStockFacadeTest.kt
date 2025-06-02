package com.example.stock.facade

import com.example.stock.domain.Stock
import com.example.stock.repository.StockRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class RedissonLockStockFacadeTest @Autowired constructor(
    private val redissonLockStockFacade: RedissonLockStockFacade,
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
    fun `동시에 100개 요청`() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        repeat(100) {
            executor.submit {
                try {
                    redissonLockStockFacade.decrease(1L, 1L)
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
