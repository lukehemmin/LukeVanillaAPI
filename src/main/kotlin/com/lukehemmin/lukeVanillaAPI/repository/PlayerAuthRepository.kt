package com.lukehemmin.lukeVanillaAPI.repository

import com.lukehemmin.lukeVanillaAPI.model.PlayerAuth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerAuthRepository : JpaRepository<PlayerAuth, String> {
    // authCode가 이제 ID이므로 findById 대신 사용할 수 있음
    fun findByUuid(uuid: String): PlayerAuth?
    fun findByAuthCode(authCode: String): PlayerAuth?
    fun existsByUuid(uuid: String): Boolean
}
