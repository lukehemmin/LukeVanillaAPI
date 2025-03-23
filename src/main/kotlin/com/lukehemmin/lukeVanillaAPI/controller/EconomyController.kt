package com.lukehemmin.lukeVanillaAPI.controller

import com.lukehemmin.lukeVanillaAPI.service.PlayerBalanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/economy")
class EconomyController(private val playerBalanceService: PlayerBalanceService) {

    @GetMapping("/balance/{uuid}")
    fun getBalance(@PathVariable uuid: String): ResponseEntity<BigDecimal> {
        val balance = playerBalanceService.getBalance(uuid)
        return ResponseEntity.ok(balance)
    }

    @PostMapping("/balance/{uuid}/add")
    fun addBalance(
        @PathVariable uuid: String, 
        @RequestParam amount: BigDecimal
    ): ResponseEntity<BigDecimal> {
        val newBalance = playerBalanceService.addBalance(uuid, amount)
        return ResponseEntity.ok(newBalance)
    }

    @PostMapping("/balance/{uuid}/remove")
    fun removeBalance(
        @PathVariable uuid: String, 
        @RequestParam amount: BigDecimal
    ): ResponseEntity<Any> {
        val success = playerBalanceService.removeBalance(uuid, amount)
        return if (success) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().body(mapOf("error" to "Insufficient balance"))
        }
    }
}
