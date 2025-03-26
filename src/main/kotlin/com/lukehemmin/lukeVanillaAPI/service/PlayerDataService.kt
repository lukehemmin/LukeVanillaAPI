package com.lukehemmin.lukeVanillaAPI.service

import com.lukehemmin.lukeVanillaAPI.dto.PlayerDataDto
import com.lukehemmin.lukeVanillaAPI.exception.ResourceNotFoundException
import com.lukehemmin.lukeVanillaAPI.model.PlayerData
import com.lukehemmin.lukeVanillaAPI.repository.PlayerDataRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlayerDataService(private val playerDataRepository: PlayerDataRepository) {

    fun getPlayerByUuid(uuid: String): PlayerDataDto {
        val playerData = playerDataRepository.findByUuid(uuid)
            ?: throw ResourceNotFoundException("Player with UUID $uuid not found")
        
        return PlayerDataDto(
            uuid = playerData.uuid,
            nickname = playerData.nickname,
            discordId = playerData.discordId
        )
    }
    
    fun getPlayerByDiscordId(discordId: String): PlayerDataDto {
        val playerData = playerDataRepository.findByDiscordId(discordId)
            ?: throw ResourceNotFoundException("Player with Discord ID $discordId not found")
        
        return PlayerDataDto(
            uuid = playerData.uuid,
            nickname = playerData.nickname,
            discordId = playerData.discordId
        )
    }
    
    @Transactional
    fun updatePlayerNickname(uuid: String, nickname: String): PlayerDataDto {
        val playerData = playerDataRepository.findByUuid(uuid)
            ?: throw ResourceNotFoundException("Player with UUID $uuid not found")
        
        playerData.nickname = nickname
        playerDataRepository.save(playerData)
        
        return PlayerDataDto(
            uuid = playerData.uuid,
            nickname = playerData.nickname,
            discordId = playerData.discordId
        )
    }
    
    @Transactional
    fun updateDiscordId(uuid: String, discordId: String): PlayerDataDto {
        // 이미 다른 플레이어가 해당 디스코드 ID를 사용 중인지 확인
        if (playerDataRepository.existsByDiscordId(discordId)) {
            throw IllegalStateException("Discord ID $discordId is already linked to another player")
        }
        
        val playerData = playerDataRepository.findByUuid(uuid)
            ?: throw ResourceNotFoundException("Player with UUID $uuid not found")
        
        playerData.discordId = discordId
        playerDataRepository.save(playerData)
        
        return PlayerDataDto(
            uuid = playerData.uuid,
            nickname = playerData.nickname,
            discordId = playerData.discordId
        )
    }
    
    @Transactional
    fun createOrUpdatePlayer(uuid: String, nickname: String, discordId: String? = null): PlayerDataDto {
        val existingPlayer = playerDataRepository.findByUuid(uuid)
        
        if (existingPlayer != null) {
            existingPlayer.nickname = nickname
            discordId?.let { existingPlayer.discordId = it }
            playerDataRepository.save(existingPlayer)
            
            return PlayerDataDto(
                uuid = existingPlayer.uuid,
                nickname = existingPlayer.nickname,
                discordId = existingPlayer.discordId
            )
        } else {
            val newPlayer = PlayerData(uuid = uuid, nickname = nickname, discordId = discordId)
            playerDataRepository.save(newPlayer)
            
            return PlayerDataDto(
                uuid = newPlayer.uuid,
                nickname = newPlayer.nickname,
                discordId = newPlayer.discordId
            )
        }
    }
}
