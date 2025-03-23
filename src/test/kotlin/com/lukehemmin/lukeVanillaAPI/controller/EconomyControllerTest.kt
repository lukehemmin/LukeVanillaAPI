package com.lukehemmin.lukeVanillaAPI.controller

import com.lukehemmin.lukeVanillaAPI.security.JwtTokenProvider
import com.lukehemmin.lukeVanillaAPI.service.PlayerBalanceService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class EconomyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var playerBalanceService: PlayerBalanceService

    @MockBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should return player balance`() {
        // given
        val uuid = UUID.randomUUID().toString()
        val balance = BigDecimal("100.50")
        
        `when`(playerBalanceService.getBalance(uuid)).thenReturn(balance)

        // when & then
        mockMvc.perform(get("/api/economy/balance/$uuid"))
            .andExpect(status().isOk)
            .andExpect(content().string("100.50"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should add balance to player account`() {
        // given
        val uuid = UUID.randomUUID().toString()
        val amount = BigDecimal("50.25")
        val newBalance = BigDecimal("150.75")
        
        `when`(playerBalanceService.addBalance(uuid, amount)).thenReturn(newBalance)

        // when & then
        mockMvc.perform(post("/api/economy/balance/$uuid/add")
                .param("amount", "50.25"))
            .andExpect(status().isOk)
            .andExpect(content().string("150.75"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should remove balance from player account`() {
        // given
        val uuid = UUID.randomUUID().toString()
        val amount = BigDecimal("30.00")
        
        `when`(playerBalanceService.removeBalance(uuid, amount)).thenReturn(true)

        // when & then
        mockMvc.perform(post("/api/economy/balance/$uuid/remove")
                .param("amount", "30.00"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should return bad request when insufficient balance`() {
        // given
        val uuid = UUID.randomUUID().toString()
        val amount = BigDecimal("200.00")
        
        `when`(playerBalanceService.removeBalance(uuid, amount)).thenReturn(false)

        // when & then
        mockMvc.perform(post("/api/economy/balance/$uuid/remove")
                .param("amount", "200.00"))
            .andExpect(status().isBadRequest)
    }
}
