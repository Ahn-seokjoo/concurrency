package com.example.stock.facade

import com.example.stock.service.StockService
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedissonLockStockFacade(
    private val stockService: StockService,
    private val redissonClient: RedissonClient,
) {

    fun decrease(id: Long, quantity: Long) {
        val lock = redissonClient.getLock(id.toString())

        try {
            val available = lock.tryLock(10, 1, TimeUnit.SECONDS)
            if (available.not()) {
                println("lock 획득 실패")
                return
            }
            stockService.decrease(id, quantity)
        } finally {
            lock.unlock()
        }
    }
}
