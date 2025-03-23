package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column

@Entity
@Table(name = "player_data")
data class PlayerData(
    @Id
    @Column(length = 36)
    val uuid: String,
    
    @Column(nullable = false)
    val nickname: String,
    
    @Column(name = "discord_id")
    var discordId: String? = null
)
