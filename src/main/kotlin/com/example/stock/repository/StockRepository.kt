package com.example.stock.repository

import com.example.stock.domain.Stock
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : JpaRepository<Stock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    fun findByIdWithPessimisticLock(id: Long): Stock

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    fun findByIdWithOptimisticLock(id: Long): Stock

    @Query("SELECT * FROM stock LIMIT 1", nativeQuery = true)
    fun findAnyOneStock(): Stock
}
