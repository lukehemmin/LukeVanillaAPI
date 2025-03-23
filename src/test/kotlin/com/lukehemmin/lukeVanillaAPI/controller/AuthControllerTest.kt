package com.lukehemmin.lukeVanillaAPI.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukehemmin.lukeVanillaAPI.dto.AuthCodeRequest
import com.lukehemmin.lukeVanillaAPI.dto.AuthCodeValidationRequest
import com.lukehemmin.lukeVanillaAPI.model.PlayerData
import com.lukehemmin.lukeVanillaAPI.service.AuthService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var authService: AuthService

    @Test
    fun `should generate auth code`() {
        // given
        val uuid = UUID.randomUUID().toString()
        val authCode = "ABC123"
        val request = AuthCodeRequest(uuid)
        
        `when`(authService.generateAuthCode(uuid)).thenReturn(authCode)

        // when & then
        mockMvc.perform(post("/api/auth/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.authCode").value(authCode))
    }

    @Test
    fun `should validate auth code and return token`() {
        // given
        val authCode = "ABC123"
        val discordId = "12345678901234567"
        val token = "jwt-token"
        val request = AuthCodeValidationRequest(authCode, discordId)
        
        `when`(authService.validateAuthCode(authCode, discordId)).thenReturn(token)

        // when & then
        mockMvc.perform(post("/api/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value(token))
    }

    @Test
    fun `should return player data by discord id when exists`() {
        // given
        val discordId = "12345678901234567"
        val uuid = UUID.randomUUID().toString()
        val nickname = "TestPlayer"
        val playerData = PlayerData(uuid, nickname, discordId)
        
        `when`(authService.getPlayerByDiscordId(discordId)).thenReturn(playerData)

        // when & then
        mockMvc.perform(get("/api/auth/player/discord/$discordId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.uuid").value(uuid))
            .andExpect(jsonPath("$.nickname").value(nickname))
            .andExpect(jsonPath("$.discordId").value(discordId))
    }

    @Test
    fun `should return not found when player does not exist by discord id`() {
        // given
        val discordId = "nonexistentId"
        
        `when`(authService.getPlayerByDiscordId(discordId)).thenReturn(null)

        // when & then
        mockMvc.perform(get("/api/auth/player/discord/$discordId"))
            .andExpect(status().isNotFound)
    }
}
