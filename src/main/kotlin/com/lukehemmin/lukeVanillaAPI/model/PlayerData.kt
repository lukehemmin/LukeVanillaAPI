package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "player_data")
data class PlayerData(
    @Id
    @Column(length = 36)
    val uuid: String,
    
    @Column(name = "nickname", length = 255, nullable = false)
    var nickname: String,
    
    @Column(name = "discord_id", length = 255)
    var discordId: String? = null
) {
    // 생성자 없는 기본 생성자 (JPA 요구사항)
    constructor() : this("", "", null)
}
