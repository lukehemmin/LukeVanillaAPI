package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column
import java.math.BigDecimal

@Entity
@Table(name = "player_balance")
data class PlayerBalance(
    @Id
    @Column(length = 36)
    val uuid: String,

    @Column(nullable = false)
    var balance: BigDecimal
)
