package com.lukehemmin.lukeVanillaAPI.service

import com.lukehemmin.lukeVanillaAPI.model.PlayerBalance
import com.lukehemmin.lukeVanillaAPI.repository.PlayerBalanceRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class PlayerBalanceServiceTest {

    @Mock
    private lateinit var playerBalanceRepository: PlayerBalanceRepository

    @InjectMocks
    private lateinit var playerBalanceService: PlayerBalanceService

    @Test
    fun `should return balance for existing user`() {
        // given
        val uuid = "test-uuid"
        val expectedBalance = BigDecimal("100.00")
        val playerBalance = PlayerBalance(uuid, expectedBalance)
        
        `when`(playerBalanceRepository.findByUuid(uuid)).thenReturn(playerBalance)

        // when
        val result = playerBalanceService.getBalance(uuid)

        // then
        assertEquals(expectedBalance, result)
        verify(playerBalanceRepository).findByUuid(uuid)
    }

    @Test
    fun `should return zero for non-existing user`() {
        // given
        val uuid = "non-existing-uuid"
        
        `when`(playerBalanceRepository.findByUuid(uuid)).thenReturn(null)

        // when
        val result = playerBalanceService.getBalance(uuid)

        // then
        assertEquals(BigDecimal.ZERO, result)
        verify(playerBalanceRepository).findByUuid(uuid)
    }
}
