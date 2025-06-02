package com.example.stock.facade

import com.example.stock.repository.RedisLockRepository
import com.example.stock.service.StockService
import org.springframework.stereotype.Component

@Component
class LettuceLockStockFacade(
    private val stockService: StockService,
    private val redisLockRepository: RedisLockRepository,
) {

    fun decrease(id: Long, quantity: Long) {
        while (redisLockRepository.lock(id).not()) {
            // 이걸 통해 redis로 가는 부하를 줄임
            Thread.sleep(100L)
        }
        try {
            stockService.decrease(id, quantity)
        } finally {
            redisLockRepository.unlock(id)
        }
    }
}
