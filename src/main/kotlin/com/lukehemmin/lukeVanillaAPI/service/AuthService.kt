package com.lukehemmin.lukeVanillaAPI.service

import com.lukehemmin.lukeVanillaAPI.dto.AuthCodeDto
import com.lukehemmin.lukeVanillaAPI.dto.AuthValidationDto
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
    fun generateAuthCode(uuid: String): AuthCodeDto {
        // PlayerAuth 엔티티의 변경된 구조에 맞게 수정
        val existingAuth = playerAuthRepository.findByUuid(uuid)
        
        if (existingAuth != null) {
            return AuthCodeDto(uuid = uuid, authCode = existingAuth.authCode)
        } else {
            // 새 인증 레코드 생성
            val authCode = generateRandomAuthCode()
            val playerAuth = PlayerAuth(
                authCode = authCode,
                uuid = uuid,
                isAuth = false
            )
            
            playerAuthRepository.save(playerAuth)
            return AuthCodeDto(uuid = uuid, authCode = authCode)
        }
    }
    
    @Transactional
    fun validateAuthCode(authCode: String, discordId: String): Boolean {
        val playerAuth = playerAuthRepository.findByAuthCode(authCode)
            ?: throw BadCredentialsException("Invalid auth code")
        
        if (playerAuth.isAuth) {
            throw BadCredentialsException("Auth code already used")
        }
        
        // 이미 해당 디스코드 ID로 인증된 플레이어가 있는지 확인
        val existingPlayer = playerDataRepository.findByDiscordId(discordId)
        if (existingPlayer != null) {
            throw BadCredentialsException("Discord ID already linked to another player")
        }
        
        // PlayerData 업데이트 또는 생성
        val playerData = playerDataRepository.findByUuid(playerAuth.uuid)
            ?: PlayerData(uuid = playerAuth.uuid, nickname = "Unknown")
        
        playerData.discordId = discordId
        playerDataRepository.save(playerData)
        
        // Auth 상태 업데이트
        playerAuth.isAuth = true
        playerAuthRepository.save(playerAuth)
        
        return true
    }
    
    @Transactional(readOnly = true)
    fun getAuthStatus(uuid: String): Boolean {
        val playerAuth = playerAuthRepository.findByUuid(uuid)
            ?: return false
        
        return playerAuth.isAuth
    }
    
    @Transactional(readOnly = true)
    fun getAuthRecordByCode(authCode: String): AuthValidationDto? {
        val playerAuth = playerAuthRepository.findByAuthCode(authCode) ?: return null
        
        return AuthValidationDto(
            uuid = playerAuth.uuid,
            isAuth = playerAuth.isAuth,
            authCode = playerAuth.authCode
        )
    }
    
    // 6자리 랜덤 인증 코드 생성 (숫자와 대문자)
    private fun generateRandomAuthCode(): String {
        val chars = ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }
}
