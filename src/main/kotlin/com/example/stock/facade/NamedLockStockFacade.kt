package com.example.stock.facade

import com.example.stock.repository.LockRepository
import com.example.stock.service.StockService
import org.springframework.stereotype.Component

@Component
class NamedLockStockFacade(
    private val stockService: StockService,
    private val lockRepository: LockRepository,
) {

    fun decrease(id: Long, quantity: Long) {
        try {
            lockRepository.getLock(id.toString())
            stockService.decrease(id, quantity)
        } finally {
            lockRepository.releaseLock(id.toString())
        }
    }
}
