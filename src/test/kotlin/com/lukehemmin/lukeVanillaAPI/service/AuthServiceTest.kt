package com.lukehemmin.lukeVanillaAPI.service

import com.lukehemmin.lukeVanillaAPI.model.PlayerAuth
import com.lukehemmin.lukeVanillaAPI.model.PlayerData
import com.lukehemmin.lukeVanillaAPI.repository.PlayerAuthRepository
import com.lukehemmin.lukeVanillaAPI.repository.PlayerDataRepository
import com.lukehemmin.lukeVanillaAPI.security.JwtTokenProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.BadCredentialsException
import java.util.*

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    @Mock
    private lateinit var playerAuthRepository: PlayerAuthRepository

    @Mock
    private lateinit var playerDataRepository: PlayerDataRepository

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @InjectMocks
    private lateinit var authService: AuthService

    @Test
    fun `should return existing auth code when uuid already has one`() {
        // given
        val uuid = UUID.randomUUID().toString()
        val existingAuthCode = "ABC123"
        val playerAuth = PlayerAuth(existingAuthCode, uuid, false)
        
        `when`(playerAuthRepository.findByUuid(uuid)).thenReturn(playerAuth)

        // when
        val result = authService.generateAuthCode(uuid)

        // then
        assertEquals(existingAuthCode, result)
        verify(playerAuthRepository).findByUuid(uuid)
        verify(playerAuthRepository, never()).save(any())
    }

    @Test
    fun `should generate new auth code when uuid does not have one`() {
        // given
        val uuid = UUID.randomUUID().toString()
        
        `when`(playerAuthRepository.findByUuid(uuid)).thenReturn(null)
        `when`(playerAuthRepository.save(any())).thenAnswer { invocation -> invocation.getArgument(0) }

        // when
        val result = authService.generateAuthCode(uuid)

        // then
        assertNotNull(result)
        assertEquals(6, result.length)
        verify(playerAuthRepository).findByUuid(uuid)
        verify(playerAuthRepository).save(any())
    }
}
