package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column

@Entity
@Table(name = "player_auth")
data class PlayerAuth(
    @Id
    @Column(length = 6)
    val authCode: String,
    
    @Column(length = 36, nullable = false)
    val uuid: String,
    
    @Column(nullable = false)
    var isAuth: Boolean = false
)
