package com.lukehemmin.lukeVanillaAPI.repository

import com.lukehemmin.lukeVanillaAPI.model.PlayerNameTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerNameTagRepository : JpaRepository<PlayerNameTag, String> {
    fun findByUuid(uuid: String): PlayerNameTag?
}
