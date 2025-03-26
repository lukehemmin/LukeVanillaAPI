package com.lukehemmin.lukeVanillaAPI.controller

import com.lukehemmin.lukeVanillaAPI.dto.*
import com.lukehemmin.lukeVanillaAPI.service.AuthService
import com.lukehemmin.lukeVanillaAPI.service.PlayerDataService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "인증 관련 API")
class AuthController(
    private val authService: AuthService,
    private val playerDataService: PlayerDataService
) {

    @PostMapping("/code")
    @Operation(summary = "인증 코드 생성", description = "플레이어 UUID를 기반으로 인증 코드를 생성합니다.")
    fun generateAuthCode(@RequestBody request: AuthRequestDto): ResponseEntity<AuthCodeDto> {
        val authCode = authService.generateAuthCode(request.uuid)
        return ResponseEntity.ok(authCode)
    }
    
    @PostMapping("/validate")
    @Operation(summary = "인증 코드 검증", description = "인증 코드와 디스코드 ID를 검증합니다.")
    fun validateAuthCode(@RequestBody request: ValidateAuthRequestDto): ResponseEntity<Map<String, Boolean>> {
        val isValid = authService.validateAuthCode(request.authCode, request.discordId)
        return ResponseEntity.ok(mapOf("valid" to isValid))
    }
    
    @GetMapping("/status/{uuid}")
    @Operation(summary = "인증 상태 확인", description = "플레이어의 인증 상태를 확인합니다.")
    fun getAuthStatus(@PathVariable uuid: String): ResponseEntity<Map<String, Boolean>> {
        val isAuth = authService.getAuthStatus(uuid)
        return ResponseEntity.ok(mapOf("isAuth" to isAuth))
    }
    
    @GetMapping("/player/discord/{discordId}")
    @Operation(summary = "디스코드 ID로 플레이어 조회", description = "디스코드 ID를 사용하여 플레이어 정보를 조회합니다.")
    fun getPlayerByDiscordId(@PathVariable discordId: String): ResponseEntity<PlayerDataDto> {
        val player = playerDataService.getPlayerByDiscordId(discordId)
        return ResponseEntity.ok(player)
    }
}
