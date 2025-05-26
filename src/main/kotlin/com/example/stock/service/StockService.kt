package com.example.stock.service

import com.example.stock.repository.StockRepository
import org.springframework.stereotype.Service

@Service
class StockService(
    private val repository: StockRepository,
) {
    fun decrease(id: Long, quantity: Long) {
        // 조회

        // 재고 감소

        // 갱신된 값 저장
        val stock = repository.findById(id).orElseThrow()
        stock.decrease(quantity)

        repository.saveAndFlush(stock)
    }
}
