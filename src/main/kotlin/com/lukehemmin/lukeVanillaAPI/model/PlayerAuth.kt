package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "player_auth")
data class PlayerAuth(
    @Id
    @Column(name = "auth_code", length = 6)
    var authCode: String,
    
    @Column(length = 36, nullable = false)
    val uuid: String,
    
    @Column(name = "is_auth", nullable = false)
    var isAuth: Boolean = false
) {
    // 생성자 없는 기본 생성자 (JPA 요구사항)
    constructor() : this("", "", false)
}
