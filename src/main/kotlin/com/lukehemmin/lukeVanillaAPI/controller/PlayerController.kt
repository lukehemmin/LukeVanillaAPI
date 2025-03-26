package com.lukehemmin.lukeVanillaAPI.controller

import com.lukehemmin.lukeVanillaAPI.dto.PlayerDataDto
import com.lukehemmin.lukeVanillaAPI.service.PlayerDataService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/players")
@Tag(name = "Player", description = "플레이어 관리 API")
class PlayerController(private val playerDataService: PlayerDataService) {

    @GetMapping("/{uuid}")
    @Operation(summary = "UUID로 플레이어 조회", description = "UUID를 사용하여 플레이어 정보를 조회합니다.")
    fun getPlayerByUuid(@PathVariable uuid: String): ResponseEntity<PlayerDataDto> {
        val player = playerDataService.getPlayerByUuid(uuid)
        return ResponseEntity.ok(player)
    }
    
    @GetMapping("/discord/{discordId}")
    @Operation(summary = "디스코드 ID로 플레이어 조회", description = "디스코드 ID를 사용하여 플레이어 정보를 조회합니다.")
    fun getPlayerByDiscordId(@PathVariable discordId: String): ResponseEntity<PlayerDataDto> {
        val player = playerDataService.getPlayerByDiscordId(discordId)
        return ResponseEntity.ok(player)
    }
    
    @PutMapping("/{uuid}/nickname")
    @Operation(summary = "플레이어 닉네임 업데이트", description = "플레이어의 닉네임을 업데이트합니다.")
    fun updatePlayerNickname(
        @PathVariable uuid: String,
        @RequestParam nickname: String
    ): ResponseEntity<PlayerDataDto> {
        val updatedPlayer = playerDataService.updatePlayerNickname(uuid, nickname)
        return ResponseEntity.ok(updatedPlayer)
    }
    
    @PutMapping("/{uuid}/discord")
    @Operation(summary = "플레이어 디스코드 ID 업데이트", description = "플레이어의 디스코드 ID를 업데이트합니다.")
    fun updatePlayerDiscordId(
        @PathVariable uuid: String,
        @RequestParam discordId: String
    ): ResponseEntity<PlayerDataDto> {
        val updatedPlayer = playerDataService.updateDiscordId(uuid, discordId)
        return ResponseEntity.ok(updatedPlayer)
    }
    
    @PostMapping
    @Operation(summary = "플레이어 생성 또는 업데이트", description = "플레이어 정보를 생성하거나 업데이트합니다.")
    fun createOrUpdatePlayer(
        @RequestParam uuid: String,
        @RequestParam nickname: String,
        @RequestParam(required = false) discordId: String?
    ): ResponseEntity<PlayerDataDto> {
        val player = playerDataService.createOrUpdatePlayer(uuid, nickname, discordId)
        return ResponseEntity.ok(player)
    }
}
