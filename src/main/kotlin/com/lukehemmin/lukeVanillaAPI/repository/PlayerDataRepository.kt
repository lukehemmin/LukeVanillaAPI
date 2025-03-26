package com.lukehemmin.lukeVanillaAPI.repository

import com.lukehemmin.lukeVanillaAPI.model.PlayerData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerDataRepository : JpaRepository<PlayerData, String> {
    fun findByUuid(uuid: String): PlayerData?
    fun findByDiscordId(discordId: String): PlayerData?
    fun existsByDiscordId(discordId: String): Boolean
}
