package com.lukehemmin.lukeVanillaAPI.controller

import com.lukehemmin.lukeVanillaAPI.model.Setting
import com.lukehemmin.lukeVanillaAPI.service.SettingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/settings")
@Tag(name = "Settings", description = "설정 관리 API")
class SettingController(private val settingService: SettingService) {

    @GetMapping("/{settingType}")
    @Operation(summary = "설정 값 조회", description = "설정 타입을 통해 설정 값을 조회합니다.")
    fun getSettingValue(@PathVariable settingType: String): ResponseEntity<Map<String, String?>> {
        val value = settingService.getSettingValue(settingType)
        return ResponseEntity.ok(mapOf("value" to value))
    }
    
    @PutMapping("/{settingType}")
    @Operation(summary = "설정 값 업데이트", description = "설정 값을 생성하거나 업데이트합니다.")
    fun updateSetting(
        @PathVariable settingType: String,
        @RequestParam settingValue: String
    ): ResponseEntity<Setting> {
        val setting = settingService.setSetting(settingType, settingValue)
        return ResponseEntity.ok(setting)
    }
    
    @GetMapping
    @Operation(summary = "모든 설정 조회", description = "모든 설정을 조회합니다.")
    fun getAllSettings(): ResponseEntity<List<Setting>> {
        val settings = settingService.getAllSettings()
        return ResponseEntity.ok(settings)
    }
    
    @DeleteMapping("/{settingType}")
    @Operation(summary = "설정 삭제", description = "특정 설정을 삭제합니다.")
    fun deleteSetting(@PathVariable settingType: String): ResponseEntity<Void> {
        settingService.deleteSetting(settingType)
        return ResponseEntity.noContent().build()
    }
}
