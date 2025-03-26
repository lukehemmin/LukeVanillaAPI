package com.lukehemmin.lukeVanillaAPI.repository

import com.lukehemmin.lukeVanillaAPI.model.Setting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SettingRepository : JpaRepository<Setting, String> {
    fun findBySettingType(settingType: String): Setting?
}
