package com.lukehemmin.lukeVanillaAPI.controller

import com.lukehemmin.lukeVanillaAPI.dto.AuthCodeRequest
import com.lukehemmin.lukeVanillaAPI.dto.AuthCodeValidationRequest
import com.lukehemmin.lukeVanillaAPI.dto.AuthResponse
import com.lukehemmin.lukeVanillaAPI.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/code")
    fun generateAuthCode(@RequestBody request: AuthCodeRequest): ResponseEntity<AuthResponse> {
        val authCode = authService.generateAuthCode(request.uuid)
        return ResponseEntity.ok(AuthResponse(authCode))
    }

    @PostMapping("/validate")
    fun validateAuthCode(@RequestBody request: AuthCodeValidationRequest): ResponseEntity<Map<String, String>> {
        val token = authService.validateAuthCode(request.authCode, request.discordId)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @GetMapping("/player/discord/{discordId}")
    fun getPlayerByDiscordId(@PathVariable discordId: String): ResponseEntity<Any> {
        val player = authService.getPlayerByDiscordId(discordId)
        return if (player != null) {
            ResponseEntity.ok(player)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
