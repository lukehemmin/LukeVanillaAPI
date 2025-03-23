package com.lukehemmin.lukeVanillaAPI.repository

import com.lukehemmin.lukeVanillaAPI.model.PlayerAuth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerAuthRepository : JpaRepository<PlayerAuth, String> {
    fun findByUuid(uuid: String): PlayerAuth?
    fun existsByUuid(uuid: String): Boolean
}
