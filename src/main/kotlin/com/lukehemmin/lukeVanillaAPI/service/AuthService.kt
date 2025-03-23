package com.lukehemmin.lukeVanillaAPI.service

import com.lukehemmin.lukeVanillaAPI.exception.ResourceNotFoundException
import com.lukehemmin.lukeVanillaAPI.model.PlayerAuth
import com.lukehemmin.lukeVanillaAPI.model.PlayerData
import com.lukehemmin.lukeVanillaAPI.repository.PlayerAuthRepository
import com.lukehemmin.lukeVanillaAPI.repository.PlayerDataRepository
import com.lukehemmin.lukeVanillaAPI.security.JwtTokenProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AuthService(
    private val playerAuthRepository: PlayerAuthRepository,
    private val playerDataRepository: PlayerDataRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun generateAuthCode(uuid: String): String {
        // 이미 존재하는 AuthCode가 있는지 확인
        val existingAuth = playerAuthRepository.findByUuid(uuid)
        if (existingAuth != null) {
            return existingAuth.authCode
        }

        // 새로운 AuthCode 생성
        val authCode = generateRandomAuthCode()
        playerAuthRepository.save(PlayerAuth(authCode, uuid, false))
        return authCode
    }

    @Transactional
    fun validateAuthCode(authCode: String, discordId: String): String {
        val auth = playerAuthRepository.findById(authCode)
            .orElseThrow { ResourceNotFoundException("Auth code not found: $authCode") }

        // 이미 인증된 코드인 경우
        if (auth.isAuth) {
            throw BadCredentialsException("Auth code already used")
        }

        // 인증 처리
        auth.isAuth = true
        playerAuthRepository.save(auth)

        // PlayerData에 DiscordID 업데이트
        val playerData = playerDataRepository.findById(auth.uuid)
            .orElseGet {
                // UUID가 없는 경우 새로 생성
                PlayerData(auth.uuid, "Unknown", null)
            }
        
        playerData.discordId = discordId
        playerDataRepository.save(playerData)

        // JWT 토큰 생성
        return jwtTokenProvider.createToken(auth.uuid, listOf("USER"))
    }

    fun getPlayerByDiscordId(discordId: String): PlayerData? {
        return playerDataRepository.findByDiscordId(discordId)
    }

    private fun generateRandomAuthCode(): String {
        val chars = ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }
}
