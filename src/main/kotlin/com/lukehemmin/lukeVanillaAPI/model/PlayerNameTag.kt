package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column

@Entity
@Table(name = "player_nametag")
data class PlayerNameTag(
    @Id
    @Column(length = 36)
    val uuid: String,
    
    @Column(nullable = false)
    var tag: String
)
