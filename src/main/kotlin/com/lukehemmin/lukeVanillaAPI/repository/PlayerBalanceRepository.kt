package com.lukehemmin.lukeVanillaAPI.repository

import com.lukehemmin.lukeVanillaAPI.model.PlayerBalance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerBalanceRepository : JpaRepository<PlayerBalance, String> {
    fun findByUuid(uuid: String): PlayerBalance?
}
