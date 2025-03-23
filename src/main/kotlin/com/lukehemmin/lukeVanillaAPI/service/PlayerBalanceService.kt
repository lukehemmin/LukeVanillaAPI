package com.lukehemmin.lukeVanillaAPI.service

import com.lukehemmin.lukeVanillaAPI.model.PlayerBalance
import com.lukehemmin.lukeVanillaAPI.repository.PlayerBalanceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class PlayerBalanceService(private val playerBalanceRepository: PlayerBalanceRepository) {

    fun getBalance(uuid: String): BigDecimal {
        return playerBalanceRepository.findByUuid(uuid)?.balance ?: BigDecimal.ZERO
    }

    @Transactional
    fun addBalance(uuid: String, amount: BigDecimal): BigDecimal {
        val balance = playerBalanceRepository.findByUuid(uuid) 
            ?: PlayerBalance(uuid, BigDecimal.ZERO)
        
        balance.balance = balance.balance.add(amount)
        playerBalanceRepository.save(balance)
        return balance.balance
    }

    @Transactional
    fun removeBalance(uuid: String, amount: BigDecimal): Boolean {
        val balance = playerBalanceRepository.findByUuid(uuid) ?: return false
        
        if (balance.balance >= amount) {
            balance.balance = balance.balance.subtract(amount)
            playerBalanceRepository.save(balance)
            return true
        }
        return false
    }
}
