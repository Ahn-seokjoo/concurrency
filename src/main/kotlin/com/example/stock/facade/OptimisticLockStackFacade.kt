package com.example.stock.facade

import com.example.stock.service.OptimisticLockStockService
import org.springframework.stereotype.Component
import kotlin.jvm.Throws

@Component
class OptimisticLockStackFacade(
    private val stockService: OptimisticLockStockService,
) {

    @Throws(InterruptedException::class)
    fun decrease(id: Long, quantity: Long) {
        while (true) {
            try {
                stockService.decrease(id, quantity)
                break
            } catch (e: Exception) {
                Thread.sleep(50L)
            }
        }
    }
}
