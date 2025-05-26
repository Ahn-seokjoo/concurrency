package com.example.stock.service

import com.example.stock.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(
    private val repository: StockRepository,
) {
    @Synchronized // 하나의 프로세스에서만 유효함
    @Transactional
    fun decrease(id: Long, quantity: Long) {
        // 조회

        // 재고 감소

        // 갱신된 값 저장
        val stock = repository.findById(id).orElseThrow()
        stock.decrease(quantity)

        repository.saveAndFlush(stock)
    }
}
