package com.lukehemmin.lukeVanillaAPI.service

import com.lukehemmin.lukeVanillaAPI.model.Setting
import com.lukehemmin.lukeVanillaAPI.repository.SettingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SettingService(private val settingRepository: SettingRepository) {

    fun getSettingValue(settingType: String): String? {
        return settingRepository.findBySettingType(settingType)?.settingValue
    }
    
    @Transactional
    fun setSetting(settingType: String, settingValue: String): Setting {
        val existingSetting = settingRepository.findBySettingType(settingType)
        
        return if (existingSetting != null) {
            existingSetting.settingValue = settingValue
            settingRepository.save(existingSetting)
        } else {
            val newSetting = Setting(settingType = settingType, settingValue = settingValue)
            settingRepository.save(newSetting)
        }
    }
    
    fun getAllSettings(): List<Setting> {
        return settingRepository.findAll()
    }
    
    @Transactional
    fun deleteSetting(settingType: String) {
        settingRepository.deleteById(settingType)
    }
}
