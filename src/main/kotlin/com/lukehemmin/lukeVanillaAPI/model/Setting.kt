package com.lukehemmin.lukeVanillaAPI.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "settings") // 소문자로 수정
data class Setting(
    @Id
    @Column(name = "setting_type", length = 50)
    val settingType: String,
    
    @Column(name = "setting_value", nullable = false, columnDefinition = "TEXT")
    var settingValue: String
) {
    // 생성자 없는 기본 생성자 (JPA 요구사항)
    constructor() : this("", "")
}
