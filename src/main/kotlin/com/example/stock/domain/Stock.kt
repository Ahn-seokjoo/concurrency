package com.example.stock.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L,

    private val productId: Long,

    var quantity: Long,
) {
    fun decrease(quantity: Long) {
        if (this.quantity - quantity < 0) throw RuntimeException("0개 미만이 되면 안댐")

        this.quantity -= quantity;
    }
}
